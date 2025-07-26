-- SET DEFINE OFF;

/*==============================================================================
  1.  LOOKUP TABLES
==============================================================================*/

CREATE TABLE user_role
(
	id         NUMBER(5)
		CONSTRAINT pk_user_role PRIMARY KEY,
	code       VARCHAR2(20)
		CONSTRAINT ux_user_role_code UNIQUE NOT NULL,
	-- audit ----
	created_at TIMESTAMP                  NOT NULL,
	created_by VARCHAR2(50)               NOT NULL,
	updated_at TIMESTAMP,
	updated_by VARCHAR2(50)
);

CREATE TABLE book_condition
(
	id         NUMBER(5)
		CONSTRAINT pk_book_condition PRIMARY KEY,
	code       VARCHAR2(20)
		CONSTRAINT ux_book_condition_code UNIQUE NOT NULL,
	-- audit ----
	created_at TIMESTAMP                       NOT NULL,
	created_by VARCHAR2(50)                    NOT NULL,
	updated_at TIMESTAMP,
	updated_by VARCHAR2(50)
);

CREATE TABLE listing_type
(
	id         NUMBER(5)
		CONSTRAINT pk_listing_type PRIMARY KEY,
	code       VARCHAR2(10)
		CONSTRAINT ux_listing_type_code UNIQUE NOT NULL,
	-- audit ----
	created_at TIMESTAMP                     NOT NULL,
	created_by VARCHAR2(50)                  NOT NULL,
	updated_at TIMESTAMP,
	updated_by VARCHAR2(50)
);

CREATE TABLE request_status
(
	id         NUMBER(5)
		CONSTRAINT pk_request_status PRIMARY KEY,
	code       VARCHAR2(15)
		CONSTRAINT ux_request_status_code UNIQUE NOT NULL,
	-- audit ----
	created_at TIMESTAMP                       NOT NULL,
	created_by VARCHAR2(50)                    NOT NULL,
	updated_at TIMESTAMP,
	updated_by VARCHAR2(50)
);

CREATE TABLE penalty_type
(
	id         NUMBER(5)
		CONSTRAINT pk_penalty_type PRIMARY KEY,
	code       VARCHAR2(20)
		CONSTRAINT ux_penalty_type_code UNIQUE NOT NULL,
	-- audit ----
	created_at TIMESTAMP                     NOT NULL,
	created_by VARCHAR2(50)                  NOT NULL,
	updated_at TIMESTAMP,
	updated_by VARCHAR2(50)
);

/*==============================================================================
  2.  CORE TABLES
==============================================================================*/

CREATE TABLE app_user
(
	id         NUMBER(19)
		GENERATED ALWAYS AS IDENTITY
		CONSTRAINT pk_user PRIMARY KEY,
	email      VARCHAR2(255)
		CONSTRAINT ux_user_email UNIQUE                            NOT NULL,
	password   VARCHAR2(255)                                     NOT NULL,
	name       VARCHAR2(100)                                     NOT NULL,
	role_id    NUMBER(5)                                         NOT NULL,
	is_deleted NUMBER(1)
		DEFAULT 0
		CONSTRAINT ck_user_is_deleted CHECK (is_deleted IN (0, 1)) NOT NULL,
	-- audit ----
	created_at TIMESTAMP                                         NOT NULL,
	created_by VARCHAR2(50)                                      NOT NULL,
	updated_at TIMESTAMP,
	updated_by VARCHAR2(50),
	deleted_at TIMESTAMP,
	deleted_by VARCHAR2(50),
	CONSTRAINT fk_user__user_role
		FOREIGN KEY (role_id) REFERENCES user_role (id)
);

CREATE TABLE book
(
	id           NUMBER(19)
		GENERATED ALWAYS AS IDENTITY
		CONSTRAINT pk_book PRIMARY KEY,
	owner_id     NUMBER(19)          NOT NULL,
	title        VARCHAR2(255)       NOT NULL,
	author       VARCHAR2(255)       NOT NULL,
	genre        VARCHAR2(100),
	isbn         VARCHAR2(20),
	description  VARCHAR2(1000),
	condition_id NUMBER(5) DEFAULT 1 NOT NULL,
	is_deleted   NUMBER(1) DEFAULT 0 NOT NULL
		CONSTRAINT ck_book_is_del CHECK (is_deleted IN (0, 1)),
	-- audit ----
	created_at   TIMESTAMP           NOT NULL,
	created_by   VARCHAR2(50)        NOT NULL,
	updated_at   TIMESTAMP,
	updated_by   VARCHAR2(50),
	deleted_at   TIMESTAMP,
	deleted_by   VARCHAR2(50),
	CONSTRAINT fk_book__user
		FOREIGN KEY (owner_id) REFERENCES app_user (id)
			ON DELETE CASCADE,
	CONSTRAINT fk_book__condition
		FOREIGN KEY (condition_id) REFERENCES book_condition (id)
);

CREATE TABLE listing
(
	id             NUMBER(19)
		GENERATED ALWAYS AS IDENTITY
		CONSTRAINT pk_listing PRIMARY KEY,
	book_id        NUMBER(19)                                     NOT NULL,
	type_id        NUMBER(5)                                      NOT NULL,
	price          NUMBER(10, 2),
	rental_days    NUMBER(5),
	is_active      NUMBER(1)
													 DEFAULT 1
		CONSTRAINT ck_listing_is_active CHECK (is_active IN (0, 1)) NOT NULL,
	-- virtual to enforce single active listing
	active_book_id NUMBER(19)
		GENERATED ALWAYS AS
			(CASE WHEN is_active = 1 THEN book_id END) VIRTUAL,
	is_deleted     NUMBER(1) DEFAULT 0                            NOT NULL
		CONSTRAINT ck_listing_is_del CHECK (is_deleted IN (0, 1)),
	-- audit ----
	created_at     TIMESTAMP                                      NOT NULL,
	created_by     VARCHAR2(50)                                   NOT NULL,
	updated_at     TIMESTAMP,
	updated_by     VARCHAR2(50),
	deleted_at     TIMESTAMP,
	deleted_by     VARCHAR2(50),
	CONSTRAINT fk_listing__book
		FOREIGN KEY (book_id) REFERENCES book (id)
			ON DELETE CASCADE,
	CONSTRAINT fk_listing__listing_type
		FOREIGN KEY (type_id) REFERENCES listing_type (id),
	CONSTRAINT ck_listing_lend_sale
		CHECK (
			(type_id = 1 AND rental_days IS NOT NULL) OR
			(type_id = 2 AND rental_days IS NULL)
			),
	CONSTRAINT ck_listing_price CHECK (price IS NULL OR price >= 0),
	CONSTRAINT uq_active_listing UNIQUE (active_book_id)
);

CREATE TABLE borrow_request
(
	id                  NUMBER(19) GENERATED ALWAYS AS IDENTITY
		CONSTRAINT pk_borrow_request PRIMARY KEY,
	listing_id          NUMBER(19)                     NOT NULL,
	borrower_id         NUMBER(19)                     NOT NULL,
	status_id           NUMBER(5) DEFAULT 1            NOT NULL,
	requested_at        TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
	approved_at         TIMESTAMP,
	rejected_at         TIMESTAMP,
	returned_at         TIMESTAMP,
	approved_listing_id NUMBER(19)
		GENERATED ALWAYS AS (CASE WHEN status_id = 2 THEN listing_id END) VIRTUAL,
	is_deleted          NUMBER(1) DEFAULT 0            NOT NULL
		CONSTRAINT ck_breq_is_del CHECK (is_deleted IN (0, 1)),
	-- audit ----
	created_at          TIMESTAMP                      NOT NULL,
	created_by          VARCHAR2(50)                   NOT NULL,
	updated_at          TIMESTAMP,
	updated_by          VARCHAR2(50),
	deleted_at          TIMESTAMP,
	deleted_by          VARCHAR2(50),
	CONSTRAINT fk_breq__listing
		FOREIGN KEY (listing_id) REFERENCES listing (id) ON DELETE CASCADE,
	CONSTRAINT fk_breq__user
		FOREIGN KEY (borrower_id) REFERENCES app_user (id) ON DELETE CASCADE,
	CONSTRAINT fk_breq__status
		FOREIGN KEY (status_id) REFERENCES request_status (id),
	CONSTRAINT uq_one_approved_per_listing UNIQUE (approved_listing_id)
);


CREATE TABLE penalty
(
	id             NUMBER(19)
		GENERATED ALWAYS AS IDENTITY
		CONSTRAINT pk_penalty PRIMARY KEY,
	request_id     NUMBER(19)              NOT NULL
		CONSTRAINT uq_penalty_request UNIQUE,
	type_id        NUMBER(5)               NOT NULL,
	amount         NUMBER(10, 2) DEFAULT 0
		CONSTRAINT ck_penalty_amount CHECK (amount >= 0),
	reason         VARCHAR2(500),
	resolved_by_id NUMBER(19),
	resolved_at    TIMESTAMP,
	is_deleted     NUMBER(1)     DEFAULT 0 NOT NULL
		CONSTRAINT ck_penalty_is_del CHECK (is_deleted IN (0, 1)),
	-- audit ----
	created_at     TIMESTAMP               NOT NULL,
	created_by     VARCHAR2(50)            NOT NULL,
	updated_at     TIMESTAMP,
	updated_by     VARCHAR2(50),
	deleted_at     TIMESTAMP,
	deleted_by     VARCHAR2(50),
	CONSTRAINT fk_penalty__breq
		FOREIGN KEY (request_id) REFERENCES borrow_request (id)
			ON DELETE CASCADE,
	CONSTRAINT fk_penalty__ptype
		FOREIGN KEY (type_id) REFERENCES penalty_type (id),
	CONSTRAINT fk_penalty__admin
		FOREIGN KEY (resolved_by_id) REFERENCES app_user (id)
			ON DELETE SET NULL
);


/*==============================================================================
  4.  INDEXES
==============================================================================*/

CREATE INDEX ix_book_owner ON book (owner_id);
CREATE INDEX ix_book_active_owner ON book (is_deleted, owner_id);
CREATE INDEX ix_book_condition ON book (condition_id);

CREATE INDEX ix_listing_book ON listing (book_id);
CREATE INDEX ix_listing_type ON listing (type_id);
CREATE INDEX ix_listing_active ON listing (is_active);
CREATE INDEX ix_listing_not_deleted ON listing (is_deleted, book_id, is_active);

CREATE INDEX ix_breq_listing ON borrow_request (listing_id);
CREATE INDEX ix_breq_borrower ON borrow_request (borrower_id);
CREATE INDEX ix_breq_status ON borrow_request (status_id);
CREATE INDEX ix_breq_active ON borrow_request (is_deleted, listing_id, status_id);

CREATE INDEX ix_penalty_type ON penalty (type_id);
CREATE INDEX ix_penalty_resolver ON penalty (resolved_by_id);
CREATE INDEX ix_penalty_active ON penalty (is_deleted, request_id);


/*==============================================================================
  3.  BUSINESS TRIGGERS
==============================================================================*/

-- Prevent borrower requesting own book
CREATE OR REPLACE TRIGGER trg_breq_no_self_request
	BEFORE INSERT OR UPDATE OF borrower_id, listing_id
	ON borrow_request
	FOR EACH ROW
DECLARE
	v_owner NUMBER(19);
BEGIN
	SELECT b.owner_id
	INTO v_owner
	FROM listing l
				 JOIN book b ON b.id = l.book_id
	WHERE l.id = :NEW.listing_id;
	IF :NEW.borrower_id = v_owner THEN
		RAISE_APPLICATION_ERROR(-20001,
														'Owner cannot request their own book');
	END IF;
END;
/

/*==============================================================================
  4.  STATIC LOOKUP DATA
==============================================================================*/

INSERT INTO user_role(id, code, created_at, created_by)
VALUES (1, 'REGULAR', SYSTIMESTAMP, 'DBA');

INSERT INTO user_role(id, code, created_at, created_by)
VALUES (2, 'ADMIN', SYSTIMESTAMP, 'DBA');

INSERT INTO book_condition(id, code, created_at, created_by)
VALUES (1, 'NEW', SYSTIMESTAMP, 'DBA');
INSERT INTO book_condition(id, code, created_at, created_by)
VALUES (2, 'GOOD', SYSTIMESTAMP, 'DBA');
INSERT INTO book_condition(id, code, created_at, created_by)
VALUES (3, 'WORN', SYSTIMESTAMP, 'DBA');

INSERT INTO listing_type(id, code, created_at, created_by)
VALUES (1, 'LEND', SYSTIMESTAMP, 'DBA');
INSERT INTO listing_type(id, code, created_at, created_by)
VALUES (2, 'SALE', SYSTIMESTAMP, 'DBA');

INSERT INTO request_status(id, code, created_at, created_by)
VALUES (1, 'PENDING', SYSTIMESTAMP, 'DBA');
INSERT INTO request_status(id, code, created_at, created_by)
VALUES (2, 'APPROVED', SYSTIMESTAMP, 'DBA');
INSERT INTO request_status(id, code, created_at, created_by)
VALUES (3, 'REJECTED', SYSTIMESTAMP, 'DBA');
INSERT INTO request_status(id, code, created_at, created_by)
VALUES (4, 'RETURNED', SYSTIMESTAMP, 'DBA');

INSERT INTO penalty_type(id, code, created_at, created_by)
VALUES (1, 'LATE_FEE', SYSTIMESTAMP, 'DBA');
INSERT INTO penalty_type(id, code, created_at, created_by)
VALUES (2, 'DAMAGE_FEE', SYSTIMESTAMP, 'DBA');

COMMIT;
