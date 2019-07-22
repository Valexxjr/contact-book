CREATE USER 'alex'@'localhost' IDENTIFIED BY 'itech';

CREATE DATABASE `contacts`
  CHARACTER SET 'utf8'
  COLLATE 'utf8_general_ci';

CREATE TABLE `contacts`.`contact` (
  `id` INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `patronymic` varchar(50),
  `birth_date` date,
  `gender` enum('male', 'female'),
  `marital_status` enum('single', 'married', 'divorced', 'widowed'),
  `citizenship` varchar(50),
  `website` varchar(50),
  `email` varchar(50),
  `place_of_work` varchar(50),
  `country` varchar(50),
  `city` varchar(50),
  `street` varchar(50),
  `zip_code` varchar(10),
  `image` varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`phone` (
  `id` INT(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  `contact_id` INT(11) UNSIGNED NOT NULL,
  `phone_number` varchar(20),
  `type` enum('home', 'mobile'),
  `note` varchar(100),
  FOREIGN KEY(`contact_id`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE
  ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `contacts`.`attachment` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `contact_id` INT(11) UNSIGNED NOT NULL,
  `file_name` varchar(100),
  `upload_date` date,
  `note` varchar(100),
  FOREIGN KEY(`contact_id`) REFERENCES `contacts`.`contact`(`id`) ON DELETE CASCADE
  ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;