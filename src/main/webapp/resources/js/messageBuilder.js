let selectIfUnselected = function () {
    if ($(PF('commentAccordionWidget').jqId).children('.ui-state-active').length === 0) {
        PF('commentAccordionWidget').select(0);
    }
}


