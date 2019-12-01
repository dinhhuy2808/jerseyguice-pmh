CREATE TABLE `can` (
  `A` int(11) NOT NULL,
  `CAN` varchar(45) DEFAULT NULL,
  `AC` int(11) DEFAULT NULL,
  PRIMARY KEY (`A`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `cart` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `payment_id` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `disct_price` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT '',
  `size` varchar(50) DEFAULT '',
  `code` varchar(20) DEFAULT '',
  `pos` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `category` (
  `cat_id` int(11) NOT NULL AUTO_INCREMENT,
  `folder_id` int(11) NOT NULL,
  `cat_name` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `cat_name_UNIQUE` (`cat_name`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

CREATE TABLE `chi` (
  `B` int(11) NOT NULL,
  `CHI` varchar(45) DEFAULT NULL,
  `PHATHOMENH` varchar(100) DEFAULT NULL,
  `BC` int(11) DEFAULT NULL,
  PRIMARY KEY (`B`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `description` (
  `description_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`description_id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;

CREATE TABLE `discount` (
  `product_id` int(11) NOT NULL,
  `effective_date` int(11) DEFAULT NULL,
  `expired_date` int(11) DEFAULT NULL,
  `disct_price` double DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `image` (
  `product_id` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `type` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mang` (
  `id` int(11) NOT NULL,
  `MANG` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `seen_flag` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `payment` (
  `payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `sum` double DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `pay_type` varchar(1) DEFAULT NULL,
  `promotion` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `seen_flag` varchar(1) DEFAULT NULL,
  `ship` varchar(1) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `voucher` varchar(11) DEFAULT NULL,
  `shipfee` int(11) DEFAULT NULL,
  `shipcode` varchar(20) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT '',
  `name` varchar(50) DEFAULT '',
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

CREATE TABLE `places` (
  `place_id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`place_id`),
  UNIQUE KEY `place_id_UNIQUE` (`place_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

CREATE TABLE `product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `cat_id` int(11) NOT NULL,
  `create_time` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `menh` varchar(255) DEFAULT NULL,
  `tuoi` varchar(255) DEFAULT NULL,
  `mau` varchar(255) DEFAULT NULL,
  `information` varchar(255) DEFAULT NULL,
  `entity` int(11) DEFAULT NULL,
  `validFlag` varchar(1) DEFAULT '1',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

CREATE TABLE `promotion` (
  `promotion_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `effective_date` int(11) DEFAULT NULL,
  `expired_date` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `seen_flag` varchar(1) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`promotion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

CREATE TABLE `settingshop` (
  `id` int(11) NOT NULL,
  `transportMethod` varchar(100) DEFAULT NULL,
  `paymentMethod` varchar(100) DEFAULT NULL,
  `freeShip` int(11) DEFAULT NULL,
  `defaultShip` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `size` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `size` varchar(10) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `disct_price` double DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `status` (
  `status_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `thuoctinh` (
  `product_id` int(11) NOT NULL,
  `mau` varchar(255) DEFAULT NULL,
  `tuoi` varchar(255) DEFAULT NULL,
  `menh` varchar(255) DEFAULT NULL,
  `sizefrom` int(11) DEFAULT NULL,
  `sizeto` int(11) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `type` (
  `type_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `dob` int(11) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `bank_account` varchar(255) DEFAULT NULL,
  `bank_address` varchar(255) DEFAULT NULL,
  `bank` varchar(255) DEFAULT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `checked` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

CREATE TABLE `voucher` (
  `voucher_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `percent` int(11) DEFAULT NULL,
  `effective_date` int(11) DEFAULT NULL,
  `expired_date` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`voucher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

CREATE TABLE `wishlist` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `disct_price` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `status_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
