'use strict';


init();
initTabs();
loadContacts();


function init() {
    if (typeof Array.prototype.forEach != 'function') {
        Array.prototype.forEach = function (callback) {
            for (var i = 0; i < this.length; i++) {
                callback.apply(this, [this[i], i, this]);
            }
        };
    }

    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = Array.prototype.forEach;
    }
}

function initTabs() {
    window.addEventListener("popstate", function(e) {
        parseUrl(location.pathname, false);
    });

    const tabs = document.querySelectorAll('#menu > li');
    tabs.forEach(function(tab) {
        if(tab.id !== 'mail')
        tab.addEventListener('click', loadTabContent)
    });

    setTab('home');
    parseUrl('/ContactBook/home', true);
}


function setTab(tabId) {
    const tabs = document.querySelectorAll('#menu > li');
    tabs.forEach(function(tab) {
        tab.classList.remove('active')
    });
    document.querySelector('#' + tabId).classList.add('active');
}

function loadTabContent(event) {
    const parent = event.target.parentNode;
    setTab(parent.id);
    parseUrl('/ContactBook/' + parent.id, true);
}


function parseUrl(url, addEntry) {
    switch(url){
        case '/ContactBook/home':
            loadContacts();
            break;
        case '/ContactBook/edit':
            loadContactFormForCreate();
            break;
        case '/ContactBook/search':
            loadSearchForm();
            break;
        default:
            addEntry = false;
            break;
    }
    if(addEntry) {
        history.pushState(null, null, url);
    }
}

function checkboxChanged(table, btnListener) {
    var checkboxes = table.querySelectorAll('input.checkbox');
    var checked = false;
    checkboxes.forEach(function (checkbox) {
        if (checkbox.checked) {
            checked = true;
        }
    });
    if (checked)
        btnListener.disabled = false;
    else
        btnListener.disabled = true;
}

function mustacheRender(template, data) {
    Mustache.parse(template);
    return Mustache.render(template, data);
}