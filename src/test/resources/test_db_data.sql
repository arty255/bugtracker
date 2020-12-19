insert into user(uuid, version, firstName, lastName)
VALUES ('341-291-23423', 0, 'firstName', 'last name');

insert into issue(uuid,
                  version,
                  issueNumber,
                  description,
                  creationTime,
                  productVersion,
                  reproduceSteps,
                  existedResult,
                  expectedResult,
                  severity,
                  fixVersion,
                  currentState)
VALUES ('11112-31412-12131', 0, '2312312', 'description', '2010-02-02', 'test', 'no steps', 'exception', 'no exception',
        0, '', 0);

insert into issue_reportedby(issueId, userId)
VALUES ('11112-31412-12131', '341-291-23423');
