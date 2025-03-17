CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    stock_quantity INT NOT NULL DEFAULT 0,
    category VARCHAR(100),
    brand VARCHAR(100),
    sku VARCHAR(50) UNIQUE NOT NULL,
    images JSON, 
    ratings DECIMAL(3,2) DEFAULT 0.0,
    reviews JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


INSERT INTO products (`name`, price, currency, images, created_at, updated_at) VALUES
('Laptop', 9998, 'USD', '["https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQyeLzp_g2l5QeSKpM-q2Y2V6o9WCToIuCANMPQqMDa6m6XgY4WjfmGyLay7S7nbKT7sVJDPU9wEZTbVJWe5F-nK2vvd6abQLuF6E5DBX09"]', NOW(), NOW()),
('Smartphone', 8999, 'USD', '["https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500"]', NOW(), NOW()),
('Tablet', 4599, 'USD', '["https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500", "https://example.com/tablet2.jpg"]', NOW(), NOW()),
('Smartwatch', 1299, 'USD', '["https://example.com/watch1.jpg", "https://example.com/watch2.jpg"]', NOW(), NOW()),
('Wireless Earbuds', 699, 'USD', '["https://example.com/earbuds.jpg"]', NOW(), NOW()),
('Gaming Laptop', 15999, 'USD', '["https://example.com/gaming_laptop.jpg"]', NOW(), NOW()),
('Mechanical Keyboard', 249, 'USD', '["https://example.com/keyboard.jpg"]', NOW(), NOW()),
('Wireless Mouse', 99, 'USD', '["https://example.com/mouse.jpg"]', NOW(), NOW()),
('Monitor 27-inch', 3299, 'USD', '["https://example.com/monitor.jpg"]', NOW(), NOW()),
('External Hard Drive 2TB', 899, 'USD', '["https://example.com/hdd.jpg"]', NOW(), NOW()),
('Smart TV 55-inch', 4999, 'USD', '["https://example.com/smart_tv.jpg"]', NOW(), NOW()),
('Bluetooth Speaker', 299, 'USD', '["https://example.com/speaker.jpg"]', NOW(), NOW()),
('Gaming Console', 4999, 'USD', '["https://example.com/console.jpg"]', NOW(), NOW()),
('Action Camera', 1399, 'USD', '["https://example.com/camera.jpg"]', NOW(), NOW()),
('Fitness Band', 599, 'USD', '["https://example.com/fitness_band.jpg"]', NOW(), NOW()),
('Drone', 8999, 'USD', '["https://example.com/drone.jpg"]', NOW(), NOW()),
('Projector', 2999, 'USD', '["https://example.com/projector.jpg"]', NOW(), NOW()),
('VR Headset', 2499, 'USD', '["https://example.com/vr.jpg"]', NOW(), NOW()),
('E-Book Reader', 1199, 'USD', '["https://example.com/ebook_reader.jpg"]', NOW(), NOW()),
('Portable Charger', 149, 'USD', '["https://example.com/powerbank.jpg"]', NOW(), NOW());


update products set images="[\"https://vsprod.vijaysales.com/media/catalog/product/2/3/238685_1_.jpg?optimize=medium&fit=bounds&height=250&width=250\"]" where id > 10;
