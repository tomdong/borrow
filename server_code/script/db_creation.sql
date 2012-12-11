CREATE  TABLE IF NOT EXISTS `intalker`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `nickname` VARCHAR(45) NULL ,
  `email` VARCHAR(50) NULL ,
  `registertime` DATETIME NULL ,
  `permission` ENUM('normal','admin','block') NULL ,
  `reflink` VARCHAR(255) NULL ,
  `remark` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB DEFAULT CHARSET=utf8;


CREATE  TABLE IF NOT EXISTS `intalker`.`book` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `isbn` INT NULL ,
  `ownerid` INT NULL ,
  `quantity` INT NULL ,
  `preview` VARCHAR(32) NULL ,
  `description` VARCHAR(255) NULL ,
  `publiclevel` ENUM('all','myfriends','specified','onlyme') NULL ,
  `status` INT NULL ,
  `remark` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB CHARSET=utf8;


CREATE  TABLE IF NOT EXISTS `intalker`.`bookinfo` (
  `isbn` INT NOT NULL ,
  `publisher` VARCHAR(50) NULL ,
  `pagecount` INT NULL ,
  `price` FLOAT NULL ,
  `remark` VARCHAR(255) NULL ,
  PRIMARY KEY (`isbn`) )
ENGINE = InnoDB CHARSET=utf8;


CREATE  TABLE IF NOT EXISTS `intalker`.`openid` (
  `localid` INT NULL ,
  `source` ENUM('weibo', 'tencent') NULL ,
  `externalid` VARCHAR(50) NULL ,
  `openidcol` VARCHAR(45) NULL )
ENGINE = InnoDB CHARSET=utf8;