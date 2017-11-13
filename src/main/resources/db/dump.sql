SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

USE crawler;

DROP TABLE IF EXISTS `urls`;
DROP TABLE IF EXISTS `books`;

CREATE TABLE IF NOT EXISTS `urls` (
  `id`              INT(11) PRIMARY KEY   NOT NULL AUTO_INCREMENT,
  `url`             VARCHAR(2083)         NOT NULL,
  `date`            TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `result`          TINYINT(1)                     DEFAULT NULL,
  `retry`           SMALLINT(6)                    DEFAULT '5',
  `content present` TINYINT(1)                     DEFAULT TRUE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `books`
(
  `id`   INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `url`  VARCHAR(2083)       NULL,
  `book` TEXT                NULL,
  `date` TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;