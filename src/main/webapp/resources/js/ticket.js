let selectIfUnselected = function () {
    if ($(PF('CommentAccordionWidget').jqId).children('.ui-state-active').length === 0) {
        PF('CommentAccordionWidget').select(0);
    }
}
