drop table if exists user;
create table if not exists user
(
    uuid      varchar(40) not null,
    version   tinyint,
    firstName varchar(50),
    lastName  varchar(50),
    primary key (uuid)
);

drop table if exists message;
create table if not exists message
(
    uuid           varchar(40) not null,
    version        int,
    title          varchar(200),
    content        varchar(1500),
    messageCreator varchar(40) not null,
    messageDate    datetime    not null,
    primary key (uuid),
    foreign key (messageCreator) references user (uuid) on delete cascade
);

drop table if exists issue;
create table if not exists issue
(
    uuid                varchar(40) not null,
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

drop table if exists issue_assigned;
create table if not exists issue_assigned
(
    issueId varchar(40) not null,
    userId  varchar(40),
    primary key (issueId, userId),
    foreign key (issueId) references issue (uuid) on delete cascade
);

drop table if exists issue_reportedBy;
create table if not exists issue_reportedBy
(
    issueId varchar(40) not null,
    userId  varchar(40) not null,
    primary key (issueId, userId),
    foreign key (issueId) references issue (uuid) on delete cascade,
    foreign key (userId) references user (uuid) on delete cascade
);

drop table if exists historyEvent;
create table if not exists historyEvent
(
    uuid      varchar(40) not null,
    version   int,
    eventDate datetime    not null,
    userId    varchar(40),
    primary key (uuid)
);

drop table if exists issueMessageEvent;
create table if not exists issueMessageEvent
(
    uuid      varchar(40) not null,
    messageId long        not null,
    primary key (uuid)
);

drop table if exists issueStateChangeEvent;
create table if not exists issueStateChangeEvent
(
    uuid               varchar(40) not null,
    state              tinyint,
    expectedFixVersion varchar(70),
    redactorId         varchar(40),
    primary key (uuid),
    foreign key (redactorId) references user (uuid) on delete cascade
);

drop table if exists issue_historyEvent;
create table if not exists issue_historyEvent
(
    issueId   varchar(40) not null,
    historyId varchar(40) not null,
    primary key (issueId, historyId)
);