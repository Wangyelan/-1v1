-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema threecountieskill
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema threecountieskill
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `threecountieskill` DEFAULT CHARACTER SET utf8 ;
USE `threecountieskill` ;

-- -----------------------------------------------------
-- Table `threecountieskill`.`card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `threecountieskill`.`card` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `flower_color` VARCHAR(45) NOT NULL,
  `point` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
