-- AEO/GEO Workshop Seed Data
-- Realistic product data for Electronics and Apparel categories

-- ============================================================================
-- MANUFACTURERS
-- ============================================================================
INSERT INTO manufacturers (id, code, name, description, website, logo_url) VALUES
(1, 'sony', 'Sony', 'Leading electronics manufacturer known for cameras, TVs, and audio equipment', 'https://www.sony.com', 'https://example.com/logos/sony.png'),
(2, 'canon', 'Canon', 'Premier imaging and optical products manufacturer', 'https://www.canon.com', 'https://example.com/logos/canon.png'),
(3, 'nikon', 'Nikon', 'Renowned camera and optics manufacturer', 'https://www.nikon.com', 'https://example.com/logos/nikon.png'),
(4, 'dell', 'Dell', 'Global computer technology company', 'https://www.dell.com', 'https://example.com/logos/dell.png'),
(5, 'apple', 'Apple', 'Consumer electronics and software leader', 'https://www.apple.com', 'https://example.com/logos/apple.png'),
(6, 'nike', 'Nike', 'World''s leading athletic footwear and apparel brand', 'https://www.nike.com', 'https://example.com/logos/nike.png'),
(7, 'adidas', 'Adidas', 'Global sportswear and athletic brand', 'https://www.adidas.com', 'https://example.com/logos/adidas.png'),
(8, 'patagonia', 'Patagonia', 'Outdoor clothing and gear company', 'https://www.patagonia.com', 'https://example.com/logos/patagonia.png'),
(9, 'samsung', 'Samsung', 'Multinational electronics conglomerate', 'https://www.samsung.com', 'https://example.com/logos/samsung.png'),
(10, 'lg', 'LG', 'Electronics and home appliances manufacturer', 'https://www.lg.com', 'https://example.com/logos/lg.png');

-- ============================================================================
-- CATEGORIES
-- ============================================================================
INSERT INTO categories (id, code, name, description, parent_id) VALUES
-- Root categories
(1, 'electronics', 'Electronics', 'Electronic devices and accessories', NULL),
(2, 'apparel', 'Apparel', 'Clothing and footwear', NULL),

-- Electronics subcategories
(3, 'cameras', 'Cameras', 'Digital cameras and photography equipment', 1),
(4, 'laptops', 'Laptops', 'Portable computers', 1),
(5, 'smartphones', 'Smartphones', 'Mobile phones', 1),
(6, 'tvs', 'TVs', 'Televisions and displays', 1),

-- Camera subcategories
(7, 'dslr', 'DSLR Cameras', 'Digital Single-Lens Reflex cameras', 3),
(8, 'mirrorless', 'Mirrorless Cameras', 'Mirrorless interchangeable lens cameras', 3),

-- Apparel subcategories
(9, 'footwear', 'Footwear', 'Shoes and athletic footwear', 2),
(10, 'jackets', 'Jackets', 'Outerwear and jackets', 2),
(11, 'activewear', 'Activewear', 'Athletic and sportswear', 2);

-- ============================================================================
-- PRODUCTS - Electronics (Cameras)
-- ============================================================================
INSERT INTO products (id, code, name, description, manufacturer_id, category_id, price, currency_code, stock_level, average_rating, review_count) VALUES
(1, 'sony-a7iv', 'Sony Alpha 7 IV', 'Full-frame mirrorless camera with 33MP sensor, 4K 60p video, advanced autofocus. Perfect for professionals and enthusiasts seeking versatility in both photo and video work.', 1, 8, 2498.00, 'USD', 45, 4.8, 324),
(2, 'canon-r5', 'Canon EOS R5', 'Professional mirrorless camera with 45MP sensor, 8K video, in-body stabilization. Industry-leading image quality and revolutionary video capabilities for content creators.', 2, 8, 3899.00, 'USD', 23, 4.9, 187),
(3, 'nikon-z9', 'Nikon Z9', 'Flagship mirrorless camera with 45.7MP stacked sensor, 8K video, no mechanical shutter. The ultimate tool for professional photographers and videographers.', 3, 8, 5496.00, 'USD', 12, 4.9, 156),
(4, 'sony-zv-e10', 'Sony ZV-E10', 'Vlogger-focused mirrorless camera with flip screen, excellent autofocus, and compact design. Purpose-built for content creators and social media professionals.', 1, 8, 698.00, 'USD', 78, 4.6, 892),
(5, 'canon-90d', 'Canon EOS 90D', 'APS-C DSLR with 32.5MP sensor, 4K video, fast continuous shooting. Versatile camera for wildlife, sports, and everyday photography.', 2, 7, 1199.00, 'USD', 34, 4.7, 445),

-- Electronics (Laptops)
(6, 'dell-xps15', 'Dell XPS 15', '15.6" laptop with Intel i7, 32GB RAM, NVIDIA RTX 3050 Ti, stunning InfinityEdge display. Premium ultraportable for creators and professionals.', 4, 4, 2299.00, 'USD', 67, 4.7, 1203),
(7, 'macbook-pro-14', 'MacBook Pro 14"', 'M3 Pro chip, 18GB RAM, 512GB SSD, Liquid Retina XDR display. Exceptional performance and battery life for developers and creative professionals.', 5, 4, 1999.00, 'USD', 89, 4.9, 2567),
(8, 'dell-xps13', 'Dell XPS 13', '13.4" ultrabook with Intel i5, 16GB RAM, 512GB SSD. Perfect balance of portability and performance for mobile professionals.', 4, 4, 1099.00, 'USD', 145, 4.6, 876),
(9, 'macbook-air-m2', 'MacBook Air M2', 'M2 chip, 8GB RAM, 256GB SSD, stunning Retina display. Incredibly thin and light laptop with all-day battery life.', 5, 4, 1199.00, 'USD', 234, 4.8, 3421),

-- Electronics (Smartphones)
(10, 'iphone-15-pro', 'iPhone 15 Pro', 'A17 Pro chip, titanium design, advanced camera system, Action button. The most powerful iPhone ever made.', 5, 5, 999.00, 'USD', 456, 4.7, 5678),
(11, 'samsung-s24-ultra', 'Samsung Galaxy S24 Ultra', 'Snapdragon 8 Gen 3, 200MP camera, S Pen, AI features. Premium Android flagship with cutting-edge technology.', 9, 5, 1199.00, 'USD', 342, 4.8, 4123),

-- Electronics (TVs)
(12, 'sony-a95l', 'Sony A95L OLED 65"', 'QD-OLED panel, Cognitive Processor XR, perfect blacks, vivid colors. Premium TV for cinephiles and gamers.', 1, 6, 2798.00, 'USD', 23, 4.9, 234),
(13, 'lg-c3-oled', 'LG C3 OLED 55"', 'OLED Evo panel, α9 AI Processor, perfect for gaming, Dolby Vision IQ. Exceptional picture quality at a great value.', 10, 6, 1496.00, 'USD', 67, 4.8, 1876),
(14, 'samsung-qn90c', 'Samsung QN90C 65"', 'Neo QLED, Mini LED backlight, anti-glare screen, 120Hz. Bright, vibrant picture perfect for any room.', 9, 6, 1897.00, 'USD', 45, 4.7, 945),

-- ============================================================================
-- PRODUCTS - Apparel (Footwear)
-- ============================================================================
(15, 'nike-air-max-90', 'Nike Air Max 90', 'Classic sneaker with visible Air cushioning, durable leather and mesh upper. Iconic design meets modern comfort for everyday wear.', 6, 9, 130.00, 'USD', 234, 4.6, 3456),
(16, 'nike-pegasus-40', 'Nike Air Zoom Pegasus 40', 'Responsive running shoe with Zoom Air cushioning, engineered mesh. Trusted by runners worldwide for training and racing.', 6, 9, 140.00, 'USD', 456, 4.8, 5234),
(17, 'adidas-ultraboost', 'Adidas Ultraboost 23', 'Premium running shoe with Boost cushioning, Primeknit upper, Continental rubber outsole. Energy return and comfort in every step.', 7, 9, 190.00, 'USD', 345, 4.7, 4123),
(18, 'nike-metcon-9', 'Nike Metcon 9', 'Cross-training shoe with stable platform, grippy traction, durable construction. Built for high-intensity workouts and lifting.', 6, 9, 150.00, 'USD', 178, 4.9, 1234),
(19, 'adidas-samba', 'Adidas Samba Classic', 'Timeless indoor soccer shoe, suede upper, gum rubber outsole. Retro style that transcends sports and fashion.', 7, 9, 90.00, 'USD', 567, 4.5, 2345),

-- Apparel (Jackets)
(20, 'patagonia-nano-puff', 'Patagonia Nano Puff Jacket', 'Lightweight insulated jacket with PrimaLoft Gold, weather-resistant shell. Packable warmth for any adventure.', 8, 10, 249.00, 'USD', 123, 4.9, 1567),
(21, 'patagonia-torrentshell', 'Patagonia Torrentshell 3L Jacket', 'Waterproof rain jacket with H2No Performance Standard, packable hood. Reliable protection in wet conditions.', 8, 10, 179.00, 'USD', 89, 4.7, 876),

-- Apparel (Activewear)
(22, 'nike-pro-tights', 'Nike Pro Dri-FIT Tights', 'Compression tights with sweat-wicking fabric, flatlock seams. Essential base layer for training and recovery.', 6, 11, 50.00, 'USD', 678, 4.6, 4567),
(23, 'adidas-alphaskin', 'Adidas Alphaskin Compression Shirt', 'Long-sleeve compression shirt with Climacool technology, strategic ventilation. Supports muscles during intense workouts.', 7, 11, 45.00, 'USD', 543, 4.5, 2345),

-- More variety
(24, 'canon-r6-ii', 'Canon EOS R6 Mark II', '24MP full-frame mirrorless, dual card slots, exceptional low-light performance. Versatile workhorse for event and wedding photographers.', 2, 8, 2499.00, 'USD', 34, 4.8, 534),
(25, 'sony-a6400', 'Sony Alpha 6400', 'APS-C mirrorless with 24.2MP sensor, fast autofocus, 4K video. Compact powerhouse for travel and street photography.', 1, 8, 898.00, 'USD', 123, 4.7, 1876),
(26, 'nike-vaporfly-3', 'Nike Vaporfly 3', 'Elite racing shoe with ZoomX foam, carbon fiber plate. Designed to help you run your fastest marathon.', 6, 9, 250.00, 'USD', 78, 4.9, 876),
(27, 'patagonia-down-sweater', 'Patagonia Down Sweater', '800-fill-power down insulation, windproof shell, classic style. Warm, packable, and built to last.', 8, 10, 229.00, 'USD', 156, 4.8, 1234);

-- Reset sequence for products
ALTER SEQUENCE products_id_seq RESTART WITH 28;
ALTER SEQUENCE manufacturers_id_seq RESTART WITH 11;
ALTER SEQUENCE categories_id_seq RESTART WITH 12;

-- ============================================================================
-- MEDIA (Product Images)
-- ============================================================================
INSERT INTO media (id, product_id, url, media_type, alt_text, is_primary, sort_order) VALUES
-- Sony A7 IV
(1, 1, 'https://images.unsplash.com/photo-1606982219448-b54e166523d5', 'image/jpeg', 'Sony Alpha 7 IV mirrorless camera front view', true, 1),
(2, 1, 'https://images.unsplash.com/photo-1606982154041-7c8e8f2f7b7f', 'image/jpeg', 'Sony A7 IV with lens attached', false, 2),

-- Canon R5
(3, 2, 'https://images.unsplash.com/photo-1606982219448-b54e166523d5', 'image/jpeg', 'Canon EOS R5 professional mirrorless camera', true, 1),

-- Nikon Z9
(4, 3, 'https://images.unsplash.com/photo-1606982154041-7c8e8f2f7b7f', 'image/jpeg', 'Nikon Z9 flagship camera with grip', true, 1),

-- Dell XPS 15
(5, 6, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45', 'image/jpeg', 'Dell XPS 15 laptop open on desk', true, 1),
(6, 6, 'https://images.unsplash.com/photo-1593642634315-48f5414c3ad9', 'image/jpeg', 'Dell XPS 15 side profile showing thinness', false, 2),

-- MacBook Pro 14
(7, 7, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8', 'image/jpeg', 'MacBook Pro 14 inch with Liquid Retina XDR display', true, 1),

-- iPhone 15 Pro
(8, 10, 'https://images.unsplash.com/photo-1695048064767-a2f8e93e4b82', 'image/jpeg', 'iPhone 15 Pro titanium design', true, 1),
(9, 10, 'https://images.unsplash.com/photo-1695048063395-2bf02d87c7ec', 'image/jpeg', 'iPhone 15 Pro camera system closeup', false, 2),

-- Nike Air Max 90
(10, 15, 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519', 'image/jpeg', 'Nike Air Max 90 classic white colorway', true, 1),
(11, 15, 'https://images.unsplash.com/photo-1600185365926-3a2ce3cdb9eb', 'image/jpeg', 'Nike Air Max 90 visible air unit detail', false, 2),

-- Adidas Ultraboost
(12, 17, 'https://images.unsplash.com/photo-1608231387042-66d1773070a5', 'image/jpeg', 'Adidas Ultraboost 23 black colorway', true, 1),

-- Patagonia Nano Puff
(13, 20, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea', 'image/jpeg', 'Patagonia Nano Puff jacket on model', true, 1);

-- ============================================================================
-- CLASSIFICATION ATTRIBUTES (Technical Specifications)
-- ============================================================================
INSERT INTO classification_attributes (product_id, name, value, unit, sort_order) VALUES
-- Sony A7 IV
(1, 'Sensor Type', 'Full Frame CMOS', NULL, 1),
(1, 'Resolution', '33', 'MP', 2),
(1, 'Video', '4K 60p', NULL, 3),
(1, 'Autofocus Points', '759', 'points', 4),
(1, 'Weight', '658', 'g', 5),

-- Canon R5
(2, 'Sensor Type', 'Full Frame CMOS', NULL, 1),
(2, 'Resolution', '45', 'MP', 2),
(2, 'Video', '8K 30p', NULL, 3),
(2, 'Image Stabilization', '8-stop IBIS', NULL, 4),
(2, 'Weight', '738', 'g', 5),

-- Nikon Z9
(3, 'Sensor Type', 'Stacked CMOS', NULL, 1),
(3, 'Resolution', '45.7', 'MP', 2),
(3, 'Video', '8K 60p', NULL, 3),
(3, 'Continuous Shooting', '120', 'fps', 4),
(3, 'Weight', '1340', 'g', 5),

-- Dell XPS 15
(6, 'Processor', 'Intel Core i7-13700H', NULL, 1),
(6, 'RAM', '32', 'GB', 2),
(6, 'Storage', '512', 'GB SSD', 3),
(6, 'Graphics', 'NVIDIA RTX 3050 Ti', NULL, 4),
(6, 'Display', '15.6" OLED 3.5K', NULL, 5),
(6, 'Weight', '1.86', 'kg', 6),

-- MacBook Pro 14
(7, 'Chip', 'Apple M3 Pro', NULL, 1),
(7, 'RAM', '18', 'GB', 2),
(7, 'Storage', '512', 'GB SSD', 3),
(7, 'Display', '14.2" Liquid Retina XDR', NULL, 4),
(7, 'Battery Life', '18', 'hours', 5),
(7, 'Weight', '1.55', 'kg', 6),

-- iPhone 15 Pro
(10, 'Chip', 'A17 Pro', NULL, 1),
(10, 'Display', '6.1" Super Retina XDR', NULL, 2),
(10, 'Camera', '48MP Main + 12MP Ultra Wide + 12MP Telephoto', NULL, 3),
(10, 'Storage', '128/256/512GB/1TB', NULL, 4),
(10, 'Material', 'Titanium', NULL, 5),
(10, 'Weight', '187', 'g', 6),

-- Nike Air Max 90
(15, 'Upper Material', 'Leather and Mesh', NULL, 1),
(15, 'Cushioning', 'Nike Air', NULL, 2),
(15, 'Fit', 'True to size', NULL, 3),
(15, 'Use', 'Casual/Lifestyle', NULL, 4),

-- Adidas Ultraboost
(17, 'Upper Material', 'Primeknit', NULL, 1),
(17, 'Midsole', 'Boost', NULL, 2),
(17, 'Outsole', 'Continental Rubber', NULL, 3),
(17, 'Drop', '10', 'mm', 4),
(17, 'Weight', '310', 'g', 5),

-- Patagonia Nano Puff
(20, 'Insulation', 'PrimaLoft Gold 60g', NULL, 1),
(20, 'Shell', '100% Recycled Polyester', NULL, 2),
(20, 'Weight', '337', 'g', 3),
(20, 'Packable', 'Yes - into own pocket', NULL, 4);

-- ============================================================================
-- PRICE ROWS (Discounts and Special Pricing)
-- ============================================================================
INSERT INTO price_rows (product_id, price, currency_code, discount_percentage, start_date, end_date, min_quantity) VALUES
-- Black Friday deals (expired)
(1, 2498.00, 'USD', 15.00, '2024-11-24 00:00:00', '2024-11-27 23:59:59', 1),
(15, 130.00, 'USD', 20.00, '2024-11-24 00:00:00', '2024-11-27 23:59:59', 1),

-- Current active deals
(6, 2299.00, 'USD', 10.00, '2025-01-01 00:00:00', '2025-01-31 23:59:59', 1),
(17, 190.00, 'USD', 15.00, '2025-01-01 00:00:00', '2025-01-31 23:59:59', 1),
(20, 249.00, 'USD', 10.00, '2025-01-10 00:00:00', '2025-01-31 23:59:59', 1),

-- Bulk pricing
(22, 50.00, 'USD', 10.00, NULL, NULL, 5),
(23, 45.00, 'USD', 15.00, NULL, NULL, 5);

-- ============================================================================
-- FAQs (Frequently Asked Questions)
-- ============================================================================
INSERT INTO faqs (product_id, question, answer, sort_order, is_published) VALUES
-- Sony A7 IV FAQs
(1, 'What is the battery life of the Sony A7 IV?', 'The Sony A7 IV can capture approximately 520 shots per charge using the viewfinder, or up to 580 shots using the LCD screen. Video recording time is approximately 70 minutes of continuous 4K recording.', 1, true),
(1, 'Does the Sony A7 IV support dual card slots?', 'Yes, the Sony A7 IV features dual card slots supporting both SD UHS-II and CFexpress Type A cards for flexible storage options and backup recording.', 2, true),
(1, 'Is the Sony A7 IV good for video recording?', 'Absolutely! The A7 IV records 4K video at up to 60fps with 10-bit 4:2:2 color sampling. It includes S-Log3, S-Cinetone, and advanced autofocus making it excellent for professional video work.', 3, true),
(1, 'What lens mount does the Sony A7 IV use?', 'The Sony A7 IV uses the Sony E-mount, which is compatible with all Sony FE full-frame lenses as well as E-mount APS-C lenses in crop mode.', 4, true),

-- Dell XPS 15 FAQs
(6, 'Can I upgrade the RAM in the Dell XPS 15?', 'Yes, the Dell XPS 15 has two SO-DIMM slots that support up to 64GB of DDR5 RAM. The RAM is user-upgradeable by removing the bottom panel.', 1, true),
(6, 'What is the screen resolution of the Dell XPS 15?', 'The Dell XPS 15 is available with multiple display options including Full HD+ (1920x1200), 3.5K OLED (3456x2160), and 4K UHD+ (3840x2400) touchscreen options.', 2, true),
(6, 'Does the Dell XPS 15 have good battery life?', 'Yes, with the FHD+ display configuration, you can expect 10-13 hours of mixed use. The OLED and 4K displays reduce battery life to approximately 7-9 hours due to higher power consumption.', 3, true),

-- iPhone 15 Pro FAQs
(10, 'What is the Action button on iPhone 15 Pro?', 'The Action button replaces the traditional mute switch and can be customized to trigger various functions like opening the camera, activating a shortcut, turning on the flashlight, or launching any app.', 1, true),
(10, 'Does iPhone 15 Pro support USB-C?', 'Yes, iPhone 15 Pro features USB-C with USB 3.2 Gen 2 speeds up to 10 Gbps, allowing for fast file transfers and compatibility with standard USB-C accessories.', 2, true),
(10, 'How is the camera system improved on iPhone 15 Pro?', 'The iPhone 15 Pro features a 48MP main camera with second-generation sensor-shift stabilization, a 12MP ultra-wide, and a 12MP 3x telephoto. It can also shoot 48MP ProRAW and offers advanced computational photography features.', 3, true),

-- Nike Air Max 90 FAQs
(15, 'Do Nike Air Max 90 run true to size?', 'Yes, the Nike Air Max 90 generally runs true to size. However, if you have wider feet, some customers recommend going up half a size for a more comfortable fit.', 1, true),
(15, 'Are Nike Air Max 90 good for running?', 'While the Air Max 90 has cushioning, it''s designed more as a lifestyle sneaker than a performance running shoe. For serious running, consider the Nike Pegasus or Vomero lines.', 2, true),
(15, 'How do I clean my Nike Air Max 90?', 'Use a soft brush and mild soap with warm water to gently clean the upper. Avoid machine washing. For the midsole, a magic eraser works well for scuff marks. Air dry only, never use heat.', 3, true),

-- Patagonia Nano Puff FAQs
(20, 'Is the Patagonia Nano Puff waterproof?', 'The Nano Puff is water-resistant but not waterproof. It has a DWR (Durable Water Repellent) finish that sheds light rain and snow. For full waterproof protection, layer it under a rain shell like the Torrentshell.', 1, true),
(20, 'How warm is the Patagonia Nano Puff?', 'The Nano Puff provides warmth equivalent to approximately 60g of PrimaLoft Gold insulation, making it ideal for temperatures from 30-50°F as a standalone layer, or colder when layered.', 2, true),
(20, 'Can the Patagonia Nano Puff be compressed?', 'Yes, the Nano Puff is highly packable and can be stuffed into its own internal chest pocket, making it easy to pack for travel or store in a backpack.', 3, true);
