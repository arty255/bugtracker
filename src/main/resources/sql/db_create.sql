create table if not exists user
(
    uuid             binary(16) not null,
    version          int,
    firstName        varchar(50),
    lastName         varchar(50),
    registrationDate datetime   not null,
    primary key (uuid)
);

create table if not exists securityUser
(
    uuid                  binary(16) not null,
    version               int,
    username              varchar(40) unique,
    password              varchar(80),
    enabled               char(1) default 'Y',
    accountNonExpired     char(1) default 'Y',
    accountNonLocked      char(1) default 'Y',
    credentialsNonExpired char(1) default 'Y',
    userId                binary(16) not null,
    email                 varchar(50),
    primary key (uuid),
    foreign key (userId) references user (uuid) on delete cascade
);

create table if not exists securityUser_authority
(
    secUserId binary(16)  not null,
    authority varchar(40) not null,
    primary key (secUserId, authority),
    foreign key (secUserId) references securityUser (uuid) on delete cascade
);

create table if not exists message
(
    uuid           binary(16) not null,
    version        int,
    content        varchar(1500),
    messageCreator binary(16) not null,
    createDate     datetime   not null,
    messageEditor  binary(16) default null,
    editDate       datetime   default null,
    archived       char(1)    default 'N',
    primary key (uuid),
    foreign key (messageCreator) references user (uuid) on delete cascade
);

create table if not exists ticket
(
    uuid              binary(16) not null,
    version           int,
    description       varchar(1200),
    reproduceSteps    varchar(1500),
    productVersion    varchar(70),
    createdBy         binary(16) not null,
    creationTime      datetime   not null,
    voteCount         int,
    verificationState tinyint,
    resolveState      tinyint,
    archived          char(1) default 'N',
    primary key (uuid),
    foreign key (createdBy) references user (uuid) on delete cascade
);

create table if not exists ticket_message
(
    ticketId  binary(16) not null,
    messageId binary(16) not null,
    primary key (ticketId, messageId),
    foreign key (ticketId) references ticket (uuid) on delete cascade,
    foreign key (messageId) references message (uuid) on delete cascade
);

create table if not exists issue
(
    uuid              binary(16)    not null,
    version           int,
    issueNumber       varchar(50),
    description       varchar(1200) not null,
    creationTime      datetime      not null,
    productVersion    varchar(70),
    reproduceSteps    varchar(1500),
    existedResult     varchar(500),
    expectedResult    varchar(500),
    severity          tinyint,
    fixVersion        varchar(70),
    currentIssueState tinyint,
    archived          char(1) default 'N',
    primary key (uuid)
);

create table if not exists issue_assigned
(
    issueId binary(16) not null,
    userId  binary(16),
    primary key (issueId, userId),
    foreign key (issueId) references issue (uuid) on delete cascade,
    foreign key (userId) references user (uuid) on delete cascade
);

create table if not exists issue_reportedBy
(
    issueId binary(16) not null,
    userId  binary(16) not null,
    primary key (issueId, userId),
    foreign key (issueId) references issue (uuid) on delete cascade,
    foreign key (userId) references user (uuid) on delete cascade
);

create table if not exists issue_ticket
(
    issueId  binary(16) not null,
    ticketId binary(16) not null,
    primary key (issueId, ticketId),
    foreign key (issueId) references issue (uuid) on delete cascade,
    foreign key (ticketId) references ticket (uuid) on delete cascade
);

create table if not exists issueEvent
(
    uuid      binary(16) not null,
    version   int,
    eventDate datetime   not null,
    userId    binary(16),
    primary key (uuid)
);

create table if not exists issueMessageEvent
(
    uuid      binary(16) not null,
    messageId binary(16) not null,
    primary key (uuid),
    foreign key (messageId) references message (uuid) on delete cascade
);

create table if not exists issueStateChangeEvent
(
    uuid       binary(16) not null,
    issueState tinyint,
    redactorId binary(16),
    primary key (uuid),
    foreign key (redactorId) references user (uuid) on delete cascade
);

create table if not exists issue_issueEvent
(
    issueId      binary(16) not null,
    issueEventId binary(16) not null,
    primary key (issueId, issueEventId),
    foreign key (issueId) references issue (uuid) on delete cascade,
    foreign key (issueEventId) references issueEvent (uuid) on delete cascade
);