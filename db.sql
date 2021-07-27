CREATE TABLE IF NOT EXISTS users
(
    `id`       int         AUTO_INCREMENT PRIMARY KEY,
    `name`     varchar(50) NOT NULL,
    `address`  text        NOT NULL,
    `email`    varchar(50) NOT NULL,
    `username` varchar(50) NOT NULL,
    `password` varchar(100) NOT NULL,
    `registration_date` varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
   `id` int(11) AUTO_INCREMENT PRIMARY KEY,
   `name` varchar(100) NOT NULL,
   `price` varchar(50) NOT NULL,
   `category` varchar(50) NOT NULL,
   `featured` varchar(50) NOT NULL,
   `image` varchar(255) NOT NULL
);

INSERT INTO `products` (`id`, `name`, `price`, `category`, `featured`, `image`) VALUES
(1, 'one plus 6', '1049', 'mobiles', 'yes', 'img/oneplus-6.jpg'),
(2, 'Iphone X', '1699', 'mobiles', 'no', 'img/iphone-x.jpeg'),
(3, 'Pocophone F1', '899', 'mobiles', 'no', 'img/pocophone-f1.jpg'),
(4, 'Samsung S9', '1199', 'mobiles', 'yes', 'img/samsung-s9.jpg'),
(5, 'Macbook Air', '2399', 'laptops', 'yes', 'img/macbook-air.jpg'),
(6, 'Asus Rog', '1549', 'laptops', 'no', 'img/asus-rog.jpg'),
(7, 'HP Pavilion', '949', 'laptops', 'no', 'img/hp-pavilion.png'),
(8, 'Acer Predator', '3199', 'laptops', 'yes', 'img/acer-predator.jpg'),
(9, 'Xiaomi Mi Watch', '699', 'watches', 'no', 'img/xiaomi-watch.jpg'),
(10, 'Samsung LED', '1249', 'tvs', 'yes', 'img/samsung-led.jpg'),
(11, 'LG LED', '949', 'tvs', 'no', 'img/lg-led.jpg'),
(12, 'Xiaomi Mi Tv', '799', 'tvs', 'no', 'img/xiaomi_mi_tv.jpg'),
(13, 'Apple Watch', '1499', 'watches', 'yes', 'img/apple-watch.jpg'),
(14, 'Huawei Watch', '849', 'watches', 'no', 'img/huawei-watch.jpg'),
(15, 'Samsung Gear', '1259', 'watches', 'yes', 'img/samsung-gear.jpg');



