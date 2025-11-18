"""
LLM Orchestrator - Query multiple AI providers in parallel
Project 3200 - FordDirect AI Search Workshop
"""

import asyncio
import os
from typing import List, Dict, Optional
from datetime import datetime
import json
import aiohttp
from openai import AsyncOpenAI
from anthropic import AsyncAnthropic
from dotenv import load_dotenv

load_dotenv()


class LLMOrchestrator:
    """
    Orchestrates queries to multiple LLM providers (OpenAI, Anthropic, Google Gemini)
    Handles rate limiting, retries, and response logging
    """

    def __init__(self, log_responses: bool = True):
        """
        Initialize API clients

        Args:
            log_responses: Whether to save raw responses to disk
        """
        self.openai_client = AsyncOpenAI(api_key=os.getenv("OPENAI_API_KEY"))
        self.anthropic_client = AsyncAnthropic(api_key=os.getenv("ANTHROPIC_API_KEY"))
        self.google_api_key = os.getenv("GOOGLE_API_KEY")  # For Gemini via API
        self.perplexity_api_key = os.getenv("PERPLEXITY_API_KEY")

        self.log_responses = log_responses
        self.log_dir = "logs"

        if self.log_responses:
            os.makedirs(self.log_dir, exist_ok=True)

    async def query_openai(
        self, prompt: str, model: str = "gpt-4", temperature: float = 0.7, max_tokens: int = 500
    ) -> Dict[str, any]:
        """
        Query OpenAI GPT-4

        Args:
            prompt: The query text
            model: OpenAI model to use
            temperature: Sampling temperature (0-2)
            max_tokens: Maximum response tokens

        Returns:
            Dict with keys: provider, response, tokens_used, latency_ms, error
        """
        start_time = datetime.now()

        try:
            response = await self.openai_client.chat.completions.create(
                model=model,
                messages=[
                    {
                        "role": "system",
                        "content": "You are a helpful assistant that provides accurate information about automotive dealers. Be specific and factual.",
                    },
                    {"role": "user", "content": prompt},
                ],
                temperature=temperature,
                max_tokens=max_tokens,
            )

            latency_ms = (datetime.now() - start_time).total_seconds() * 1000
            result = {
                "provider": "openai",
                "model": model,
                "response": response.choices[0].message.content,
                "tokens_used": response.usage.total_tokens,
                "latency_ms": round(latency_ms, 2),
                "error": None,
            }

        except Exception as e:
            result = {
                "provider": "openai",
                "model": model,
                "response": None,
                "tokens_used": 0,
                "latency_ms": 0,
                "error": str(e),
            }

        return result

    async def query_anthropic(
        self, prompt: str, model: str = "claude-3-5-sonnet-20241022", max_tokens: int = 500
    ) -> Dict[str, any]:
        """
        Query Anthropic Claude

        Args:
            prompt: The query text
            model: Claude model to use
            max_tokens: Maximum response tokens

        Returns:
            Dict with keys: provider, response, tokens_used, latency_ms, error
        """
        start_time = datetime.now()

        try:
            response = await self.anthropic_client.messages.create(
                model=model,
                max_tokens=max_tokens,
                messages=[{"role": "user", "content": prompt}],
            )

            latency_ms = (datetime.now() - start_time).total_seconds() * 1000
            result = {
                "provider": "anthropic",
                "model": model,
                "response": response.content[0].text,
                "tokens_used": response.usage.input_tokens + response.usage.output_tokens,
                "latency_ms": round(latency_ms, 2),
                "error": None,
            }

        except Exception as e:
            result = {
                "provider": "anthropic",
                "model": model,
                "response": None,
                "tokens_used": 0,
                "latency_ms": 0,
                "error": str(e),
            }

        return result

    async def query_google(self, prompt: str, model: str = "gemini-pro") -> Dict[str, any]:
        """
        Query Google Gemini via REST API

        Args:
            prompt: The query text
            model: Gemini model to use

        Returns:
            Dict with keys: provider, response, tokens_used, latency_ms, error
        """
        start_time = datetime.now()

        if not self.google_api_key:
            return {
                "provider": "google",
                "model": model,
                "response": None,
                "tokens_used": 0,
                "latency_ms": 0,
                "error": "GOOGLE_API_KEY not set",
            }

        try:
            url = f"https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={self.google_api_key}"
            payload = {"contents": [{"parts": [{"text": prompt}]}]}

            async with aiohttp.ClientSession() as session:
                async with session.post(url, json=payload) as resp:
                    data = await resp.json()

                    if "candidates" in data and len(data["candidates"]) > 0:
                        response_text = data["candidates"][0]["content"]["parts"][0]["text"]
                        latency_ms = (datetime.now() - start_time).total_seconds() * 1000

                        result = {
                            "provider": "google",
                            "model": model,
                            "response": response_text,
                            "tokens_used": data.get("usageMetadata", {}).get("totalTokenCount", 0),
                            "latency_ms": round(latency_ms, 2),
                            "error": None,
                        }
                    else:
                        result = {
                            "provider": "google",
                            "model": model,
                            "response": None,
                            "tokens_used": 0,
                            "latency_ms": 0,
                            "error": "No candidates in response",
                        }

        except Exception as e:
            result = {
                "provider": "google",
                "model": model,
                "response": None,
                "tokens_used": 0,
                "latency_ms": 0,
                "error": str(e),
            }

        return result

    async def query_all(
        self, prompt: str, providers: Optional[List[str]] = None
    ) -> Dict[str, Dict]:
        """
        Query all LLM providers in parallel

        Args:
            prompt: The query text
            providers: List of providers to query (default: all available)

        Returns:
            Dict mapping provider name to response data
        """
        if providers is None:
            providers = ["openai", "anthropic", "google"]

        tasks = []
        if "openai" in providers and os.getenv("OPENAI_API_KEY"):
            tasks.append(self.query_openai(prompt))
        if "anthropic" in providers and os.getenv("ANTHROPIC_API_KEY"):
            tasks.append(self.query_anthropic(prompt))
        if "google" in providers and self.google_api_key:
            tasks.append(self.query_google(prompt))

        results = await asyncio.gather(*tasks, return_exceptions=True)

        # Convert list of results to dict keyed by provider
        results_dict = {}
        for result in results:
            if isinstance(result, dict):
                results_dict[result["provider"]] = result
            else:
                # Handle exceptions
                print(f"Error in query: {result}")

        # Log responses if enabled
        if self.log_responses:
            self._log_query_results(prompt, results_dict)

        return results_dict

    def _log_query_results(self, prompt: str, results: Dict):
        """Save query results to disk for analysis"""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        log_file = f"{self.log_dir}/query_{timestamp}.json"

        log_data = {"timestamp": timestamp, "prompt": prompt, "results": results}

        with open(log_file, "w") as f:
            json.dump(log_data, f, indent=2)

    async def audit_dealers(
        self, queries: List[Dict], providers: Optional[List[str]] = None
    ) -> List[Dict]:
        """
        Run full audit: query all LLMs for all queries

        Args:
            queries: List of query dicts with keys: id, text, expected_dealers, etc.
            providers: List of providers to query

        Returns:
            List of audit results
        """
        all_results = []

        for i, query in enumerate(queries):
            print(f"Processing query {i+1}/{len(queries)}: {query['text'][:50]}...")

            llm_responses = await self.query_all(query["text"], providers=providers)

            result = {
                "query_id": query.get("id", f"q{i:03d}"),
                "query_text": query["text"],
                "expected_dealers": query.get("expected_dealers", []),
                "llm_responses": llm_responses,
                "timestamp": datetime.now().isoformat(),
            }

            all_results.append(result)

            # Rate limiting: small delay between queries
            await asyncio.sleep(0.5)

        return all_results


# Example usage
async def main():
    """Example: Test the orchestrator with a sample query"""

    orchestrator = LLMOrchestrator(log_responses=True)

    test_query = "Find me a Ford dealer near Aurora, Colorado with F-150 Lariat trucks in stock under $55,000."

    print(f"Querying LLMs: {test_query}\n")

    results = await orchestrator.query_all(test_query)

    for provider, data in results.items():
        print(f"\n{'='*60}")
        print(f"Provider: {provider.upper()}")
        print(f"{'='*60}")
        if data["error"]:
            print(f"❌ Error: {data['error']}")
        else:
            print(f"✅ Response ({data['latency_ms']}ms, {data['tokens_used']} tokens):")
            print(f"{data['response'][:200]}...")


if __name__ == "__main__":
    asyncio.run(main())
