create schema h_bugtracker;
use h_bugtracker;


drop table if exists bt_user;
create table if not exists bt_user
(
    uuid      long not null,
    version   int,
    firstName varchar(50),
    lastName  varchar(50),
    primary key (uuid)
);

drop table if exists bt_message;
create table if not exists bt_message
(
    uuid           long not null,
    version        int,
    title          varchar(200),
    content        varchar(1500),
    messageCreator long not null,
    primary key (uuid),
    foreign key (messageCreator) references bt_user (uuid) on delete cascade
);

drop table if exists bt_issue;
create table if not exists bt_issue
(
    uuid                long not null,
    version             int,
    issueID             varchar(50),
    shortDescription    varchar(200),
    fullDescription     varchar(1200),
    issueAppearanceTime datetime,
    ticketCreationTime  datetime,
    productVersion      varchar(70),
    reproduceSteps      varchar(1500),
    existedResult       varchar(500),
    expectedResult      varchar(500),
    severity            tinyint,
    fixVersion          varchar(70),
    currentState        tinyint,
    primary key (uuid)
);

drop table if exists bt_issue_assigned;
create table if not exists bt_issue_assigned
(
    issueId long not null,
    userId  long,
    primary key (issueId, issueId),
    foreign key (issueId) references bt_issue (uuid) on delete cascade
);

drop table if exists bt_issue_reportedBy;
create table if not exists bt_issue_reportedBy
(
    issueId long not null,
    userId  long not null,
    primary key (issueId, issueId),
    foreign key (issueId) references bt_issue (uuid) on delete cascade,
    foreign key (userId) references bt_user (uuid) on delete cascade
);

drop table if exists bt_historyEvent;
create table if not exists bt_historyEvent
(
    uuid      long     not null,
    version   int,
    eventDate datetime not null,
    userId    long,
    primary key (uuid)
);

drop table if exists bt_issueMessageEvent;
create table if not exists bt_issueMessageEvent
(
    uuid      long not null,
    messageId long not null,
    primary key (uuid)
);

drop table if exists bt_issueStateChangeEvent;
create table if not exists bt_issueStateChangeEvent
(
    uuid               long not null,
    state              tinyint,
    expectedFixVersion varchar(70),
    redactorId         bigint,
    primary key (uuid),
    foreign key (redactorId) references bt_user (uuid) on delete cascade
);

drop table if exists bt_issue_historyEvent;
create table if not exists bt_issue_historyEvent
(
    issueId   bigint not null,
    historyId bigint not null,
    primary key (issueId, historyId)
);