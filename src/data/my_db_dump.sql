SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: accounts; Type: TABLE; Schema: public; Owner: postgres
--
CREATE TABLE public.accounts
(
    account_id integer               NOT NULL,
    username   character varying(50) NOT NULL,
    password   character varying(50) NOT NULL,
    role_id    integer DEFAULT 1     NOT NULL
);


ALTER TABLE public.accounts
    OWNER TO postgres;

--
-- Name: accounts_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.accounts_account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.accounts_account_id_seq OWNER TO postgres;

--
-- Name: accounts_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.accounts_account_id_seq OWNED BY public.accounts.account_id;


--
-- Name: permission; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permission
(
    permission_id   integer           NOT NULL,
    permission_name character varying NOT NULL
);


ALTER TABLE public.permission
    OWNER TO postgres;

--
-- Name: permission_permission_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.permission_permission_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.permission_permission_id_seq OWNER TO postgres;

--
-- Name: permission_permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.permission_permission_id_seq OWNED BY public.permission.permission_id;


--
-- Name: profiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.profiles
(
    profile_id        integer               NOT NULL,
    fullname          character varying(50) NOT NULL,
    age               smallint              NOT NULL,
    email             character varying(100),
    phone_number      character(10)         NOT NULL,
    emergency_contact character(10)         NOT NULL,
    account_id        integer               NOT NULL
);


ALTER TABLE public.profiles
    OWNER TO postgres;

--
-- Name: profiles_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.profiles_account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.profiles_account_id_seq OWNER TO postgres;

--
-- Name: profiles_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.profiles_account_id_seq OWNED BY public.profiles.account_id;


--
-- Name: profiles_profile_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.profiles_profile_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.profiles_profile_id_seq OWNER TO postgres;

--
-- Name: profiles_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.profiles_profile_id_seq OWNED BY public.profiles.profile_id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role
(
    role_id   integer           NOT NULL,
    role_name character varying NOT NULL
);


ALTER TABLE public.role
    OWNER TO postgres;

--
-- Name: role_permission_mapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role_permission_mapping
(
    rpm_id        integer NOT NULL,
    role_id       integer NOT NULL,
    permission_id integer NOT NULL
);


ALTER TABLE public.role_permission_mapping
    OWNER TO postgres;

--
-- Name: role_permission_mapping_rpm_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_permission_mapping_rpm_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.role_permission_mapping_rpm_id_seq OWNER TO postgres;

--
-- Name: role_permission_mapping_rpm_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_permission_mapping_rpm_id_seq OWNED BY public.role_permission_mapping.rpm_id;


--
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.role_role_id_seq OWNER TO postgres;

--
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_role_id_seq OWNED BY public.role.role_id;


--
-- Name: accounts account_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ALTER COLUMN account_id SET DEFAULT nextval('public.accounts_account_id_seq'::regclass);


--
-- Name: permission permission_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permission
    ALTER COLUMN permission_id SET DEFAULT nextval('public.permission_permission_id_seq'::regclass);


--
-- Name: profiles profile_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profiles
    ALTER COLUMN profile_id SET DEFAULT nextval('public.profiles_profile_id_seq'::regclass);


--
-- Name: role role_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ALTER COLUMN role_id SET DEFAULT nextval('public.role_role_id_seq'::regclass);


--
-- Name: role_permission_mapping rpm_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permission_mapping
    ALTER COLUMN rpm_id SET DEFAULT nextval('public.role_permission_mapping_rpm_id_seq'::regclass);


--
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--


INSERT INTO public.accounts (account_id, username, password, role_id)
VALUES (2, 'User', '@User0123456', 2);
INSERT INTO public.accounts (account_id, username, password, role_id)
VALUES (1, 'Admin', '@Admin0123456', 1);


--
-- Data for Name: permission; Type: TABLE DATA; Schema: public; Owner: postgres
--


INSERT INTO public.permission (permission_id, permission_name)
VALUES (1, 'sign_up');
INSERT INTO public.permission (permission_id, permission_name)
VALUES (2, 'view_profile ');
INSERT INTO public.permission (permission_id, permission_name)
VALUES (3, 'view_profile_of_user');
INSERT INTO public.permission (permission_id, permission_name)
VALUES (4, 'view_list_user ');


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: postgres
--


INSERT INTO public.profiles (profile_id, fullname, age, email, phone_number, emergency_contact, account_id)
VALUES (1, 'User', 20, 'User@gmail.com', '0123456789', '0123456789', 2);
INSERT INTO public.profiles (profile_id, fullname, age, email, phone_number, emergency_contact, account_id)
VALUES (2, 'Admin', 20, 'Admin@gmail.com', '0123456788', '0123456789', 1);


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--


INSERT INTO public.role (role_id, role_name)
VALUES (1, 'Admin');
INSERT INTO public.role (role_id, role_name)
VALUES (2, 'User');


--
-- Data for Name: role_permission_mapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role_permission_mapping (rpm_id, role_id, permission_id)
VALUES (1, 1, 1);
INSERT INTO public.role_permission_mapping (rpm_id, role_id, permission_id)
VALUES (2, 1, 2);
INSERT INTO public.role_permission_mapping (rpm_id, role_id, permission_id)
VALUES (3, 1, 3);
INSERT INTO public.role_permission_mapping (rpm_id, role_id, permission_id)
VALUES (4, 1, 4);
INSERT INTO public.role_permission_mapping (rpm_id, role_id, permission_id)
VALUES (5, 2, 2);



--
-- Name: accounts_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.accounts_account_id_seq', 3, true);


--
-- Name: permission_permission_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.permission_permission_id_seq', 4, true);


--
-- Name: profiles_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.profiles_account_id_seq', 1, false);


--
-- Name: profiles_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.profiles_profile_id_seq', 3, true);


--
-- Name: role_permission_mapping_rpm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_permission_mapping_rpm_id_seq', 6, true);


--
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_role_id_seq', 2, true);


--
-- Name: permission permission_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pk PRIMARY KEY (permission_id);


--
-- Name: role_permission_mapping role_permission_mapping_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permission_mapping
    ADD CONSTRAINT role_permission_mapping_pk PRIMARY KEY (rpm_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: role_permission_mapping role_permission_mapping_permission_permission_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permission_mapping
    ADD CONSTRAINT role_permission_mapping_permission_permission_id_fk FOREIGN KEY (permission_id) REFERENCES public.permission (permission_id);


--
-- Name: role_permission_mapping role_permission_mapping_role_role_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permission_mapping
    ADD CONSTRAINT role_permission_mapping_role_role_id_fk FOREIGN KEY (role_id) REFERENCES public.role (role_id);


--
-- PostgreSQL database dump complete
--

