CREATE TABLE `report_histories`
(
    `id`          int         NOT NULL AUTO_INCREMENT,
    `market`      varchar(45) NOT NULL,
    `price`       varchar(45) NOT NULL,
    `reported_at` datetime    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci);
