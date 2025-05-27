create table eye_exercise
(
    duration    int         not null,
    id          bigint auto_increment
        primary key,
    name        varchar(20) not null,
    description varchar(50) not null
);

create table users
(
    deleted      bit         not null,
    created_at   datetime(6) not null,
    id           bigint auto_increment
        primary key,
    updated_at   datetime(6) not null,
    name         varchar(20) not null,
    phone_number varchar(50) not null,
    constraint UK9q63snka3mdh91as4io72espi
        unique (phone_number)
);

create table amsler_checks
(
    deleted           bit         not null,
    created_at        datetime(6) not null,
    id                bigint auto_increment
        primary key,
    updated_at        datetime(6) not null,
    user_id           bigint      null,
    left_macular_loc  varchar(20) not null,
    right_macular_loc varchar(20) not null,
    constraint FK7y38fx7xp5dd04ofolxjah77q
        foreign key (user_id) references users (id)
);

create table eye_exercise_records
(
    eye_exercise_id bigint not null,
    id              bigint auto_increment
        primary key,
    user_id         bigint not null,
    constraint FKdj12n7nate1h4g97q8q5pagkr
        foreign key (user_id) references users (id),
    constraint FKljvb2euxukkgy1n2hqte96gma
        foreign key (eye_exercise_id) references eye_exercise (id)
);

create table mchart_checks
(
    deleted       bit         not null,
    left_eye_hor  int         not null,
    left_eye_ver  int         not null,
    right_eye_hor int         not null,
    right_eye_ver int         not null,
    created_at    datetime(6) not null,
    id            bigint auto_increment
        primary key,
    updated_at    datetime(6) not null,
    user_id       bigint      null,
    constraint FKh028q8gi0jwpfo91y6rq2j6dk
        foreign key (user_id) references users (id)
);

create table presbyopia_checks
(
    avg_distance    double      not null,
    deleted         bit         not null,
    first_distance  double      not null,
    second_distance double      not null,
    third_distance  double      not null,
    created_at      datetime(6) not null,
    id              bigint auto_increment
        primary key,
    updated_at      datetime(6) not null,
    user_id         bigint      null,
    constraint FKkhfwkvalw96m1d2eev72571ud
        foreign key (user_id) references users (id)
);

create table report_summaries
(
    id                     bigint auto_increment
        primary key,
    user_id                bigint   not null,
    final_analysis_text    longtext null,
    recommend_next_checkup longtext null,
    constraint FKswp3nne83173obe8tr01wmq6t
        foreign key (user_id) references users (id)
);

create table sight_checks
(
    deleted           bit                                                         not null,
    left_sight        int                                                         not null,
    right_sight       int                                                         not null,
    created_at        datetime(6)                                                 not null,
    id                bigint auto_increment
        primary key,
    updated_at        datetime(6)                                                 not null,
    user_id           bigint                                                      null,
    left_perspective  enum ('ASTIGMATISM', 'FARSIGHTED', 'NEARSIGHTED', 'NORMAL') not null,
    right_perspective enum ('ASTIGMATISM', 'FARSIGHTED', 'NEARSIGHTED', 'NORMAL') not null,
    constraint FKjijeaummf88qx7p6k7b0xmeq7
        foreign key (user_id) references users (id)
);

create table surveys
(
    age        int             not null,
    deleted    bit             not null,
    diabetes   bit             not null,
    glasses    bit             not null,
    smoking    bit             not null,
    surgery    tinyint         not null,
    created_at datetime(6)     not null,
    id         bigint auto_increment
        primary key,
    updated_at datetime(6)     not null,
    user_id    bigint          null,
    gender     enum ('F', 'M') not null,
    constraint FKiydpdbdg90l5bl365gt67qgrn
        foreign key (user_id) references users (id),
    check (`surgery` between 0 and 3)
);

create table user_agrees
(
    deleted         bit         not null,
    marketing_agree bit         not null,
    privacy         bit         not null,
    service         bit         not null,
    created_at      datetime(6) not null,
    id              bigint auto_increment
        primary key,
    updated_at      datetime(6) not null,
    user_id         bigint      not null,
    constraint FKgqisxym4yd945kjxhrpais07a
        foreign key (user_id) references users (id)
);

create table webauthn_credentials
(
    deleted          bit                                    not null,
    created_at       datetime(6)                            not null,
    id               bigint auto_increment
        primary key,
    last_used_at     datetime(6)                            null,
    registered_at    datetime(6)                            not null,
    signature_count  bigint                                 not null,
    updated_at       datetime(6)                            not null,
    user_id          bigint                                 null,
    aaguid           varchar(36)                            null,
    device_type      varchar(50)                            null,
    name             varchar(64)                            not null,
    attestation_type varchar(255)                           not null,
    credential_id    varchar(255)                           not null,
    device_name      varchar(255)                           null,
    transports       varchar(255)                           null,
    public_key_cose  blob                                   not null,
    status           enum ('ACTIVE', 'INACTIVE', 'REVOKED') not null,
    constraint UKglrl82tpoldymtubv6p3nx26c
        unique (credential_id),
    constraint FK61k8kijke2qqqpsrg65qjwcie
        foreign key (user_id) references users (id)
);

