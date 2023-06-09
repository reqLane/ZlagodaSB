CREATE TABLE IF NOT EXISTS Category(
    category_number INT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (category_number)
);

CREATE TABLE IF NOT EXISTS Product(
    id_product INT NOT NULL AUTO_INCREMENT,
    category_number INT NOT NULL,
    product_name VARCHAR(50) NOT NULL UNIQUE,
    manufacturer VARCHAR(20) NOT NULL,
    characteristics VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_product),
    FOREIGN KEY (category_number) REFERENCES Category(category_number)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS Store_Product(
    UPC VARCHAR(12) NOT NULL,
    id_product INT NOT NULL,
    selling_price DECIMAL(13,4) NOT NULL CHECK (selling_price >= 0),
    products_number INT NOT NULL CHECK (products_number >= 0),
    expiration_date DATE NOT NULL,
    promotional_product BOOLEAN NOT NULL,
    PRIMARY KEY (UPC),
    FOREIGN KEY (id_product) REFERENCES Product(id_product)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS Employee(
    id_employee VARCHAR(10) NOT NULL,
    password VARCHAR(60) NOT NULL,
    empl_surname VARCHAR(50) NOT NULL,
    empl_name VARCHAR(50) NOT NULL,
    empl_patronymic VARCHAR(50) NULL,
    empl_role VARCHAR(10) NOT NULL,
    salary DECIMAL(13,4) NOT NULL CHECK (salary >= 0),
    date_of_birth DATE NOT NULL CHECK (DATEDIFF('YEAR', date_of_birth, NOW()) >= 18),
    date_of_start DATE NOT NULL,
    phone_number VARCHAR(13) NOT NULL,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    zip_code VARCHAR(9) NOT NULL,
    PRIMARY KEY(id_employee)
);

CREATE TABLE IF NOT EXISTS Customer_Card(
    card_number VARCHAR(13) NOT NULL,
    cust_surname VARCHAR(50) NOT NULL,
    cust_name VARCHAR(50) NOT NULL,
    cust_patronymic VARCHAR(50) NULL,
    phone_number VARCHAR(13) NOT NULL,
    city VARCHAR(50) NULL,
    street VARCHAR(50) NULL,
    zip_code VARCHAR(9) NULL,
    percent INT NOT NULL CHECK (percent >= 0 AND percent <= 80),
    PRIMARY KEY(card_number)
);

CREATE TABLE IF NOT EXISTS Checks(
    check_number VARCHAR(10) NOT NULL,
    id_employee VARCHAR(10) NOT NULL,
    card_number VARCHAR(13) NULL,
    print_date TIMESTAMP NOT NULL,
    sum_total DECIMAL(13,4) NOT NULL CHECK (sum_total >= 0),
    vat DECIMAL(13,4) NOT NULL CHECK (vat >= 0),
    PRIMARY KEY (check_number),
    FOREIGN KEY (id_employee) REFERENCES Employee(id_employee)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (card_number) REFERENCES Customer_Card(card_number)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS Sale(
    UPC VARCHAR(12) NOT NULL,
    check_number VARCHAR(10) NOT NULL,
    product_number INT NOT NULL CHECK (product_number >= 0),
    selling_price DECIMAL(13,4) NOT NULL CHECK (selling_price >= 0),
    PRIMARY KEY(UPC, check_number),
    FOREIGN KEY (UPC) REFERENCES Store_Product(UPC)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (check_number) REFERENCES Checks(check_number)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
