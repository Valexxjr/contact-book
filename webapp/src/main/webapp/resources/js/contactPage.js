'use strict';
function loadContactsSearch(searchParams) {
    if(!searchParams)
        searchParams = getSearchParameters();
    fetch('/ContactBook/search/length', {
        method: 'POST',
        body: JSON.stringify(searchParams)
    }).then(function(response) {
        return response.text()
    })
        .then(function (length) {
            loadContactsSearchPage(1, length, searchParams);
        });
}

function loadContacts() {
    fetch('/ContactBook/contacts/length').then(function(response) {
        return response.text();
    })
        .then(function (length) {
            loadContactsPage(1, length);
        });
}

function loadContactsSearchPage(page, length, searchParams) {
    const data = fetch('/ContactBook/search?page=' + page, {
        method: 'POST',
        body: JSON.stringify(searchParams)
    }).then(function(response) {
        return response.json()
    });
    loadContactsTable(data, length, page, searchParams);
}

function loadContactsPage(page, length) {
    const data = fetch('/ContactBook/contacts?page=' + page).then(function(response) {
        return response.json()
    });
    loadContactsTable(data, length, page, null);
}

function loadContactsTable(data, length, page, searchParams) {
    setTab('home');
    length = parseInt(length);
    const numPages = Math.floor(length / 10) + ((length % 10 === 0) ? 0 : 1);
    const template = fetch('../resources/html/contact_table.html?' + Math.random()).then(function(response) {
        return response.text()
    });
    Promise.all([data, template])
        .then(function(response) {
            const resolvedData = response[0];
            const resolvedTemplate = response[1];
            document.getElementById('main_content').innerHTML = mustacheRender(resolvedTemplate, resolvedData);

            const ul = document.querySelector('.pagination');
            for(let i = 1; i <= numPages; i++) {
                const li = document.createElement('li');
                li.classList.add('page-item');
                if(i === page)
                    li.classList.add('active');
                const a = document.createElement('a');
                a.classList.add('page-link');
                a.innerText = i;
                if(searchParams)
                    a.addEventListener('click', loadContactsSearchPage.bind(null, i, length, searchParams));
                else
                    a.addEventListener('click', loadContactsPage.bind(null, i, length));
                li.appendChild(a);
                ul.appendChild(li);
            }
            initContactPage();
        }).catch(function(error) {
        console.log('Unable to get all template data: ', error.message)
    });
}

function initContactPage() {
    const table = document.querySelector('#table_contact');
    const btnDelete = document.querySelector('#delete_contact');
    const btnEmail = document.querySelector('#send_email');
    const checkboxes = document.querySelectorAll('input.checkbox');

    checkboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function () {
            checkboxChanged(table, btnDelete);
            checkboxChanged(table, btnEmail);
        })
    });

    btnDelete.disabled = true;
    btnEmail.disabled = true;

    btnEmail.addEventListener('click', function () {
        const rows = document.querySelectorAll('tbody > tr');
        let ids = [];
        let emails = [];
        rows.forEach(function (row) {
            const checkbox = row.querySelector('input.checkbox');
            if(checkbox.checked) {
                ids.push(row.querySelector('.d-none').textContent);
                emails.push(row.querySelector('.email').textContent);
            }
        });
        loadEmailForm(ids, emails);
        history.pushState(null, null, '/ContactBook/mail')
    });

    const rows = document.querySelectorAll('tr');
    rows.forEach(function (row) {
        var id = row.querySelector('.d-none').textContent;
        var a = row.querySelector('a');
        if (a)
            a.addEventListener('click', function () {
                parseUrl('/ContactBook/edit', true);
                loadContactFormForEdit(id);
            }, false);
    });

    btnDelete.addEventListener('click', function () {
        const rows = document.querySelectorAll('tbody > tr');
        let ids = [];
        rows.forEach(function (row) {
            const checkbox = row.querySelector('input.checkbox');
            if(checkbox.checked) {
                ids.push(row.querySelector('.d-none').textContent);
            }
        });
        fetch('/ContactBook/contacts', {
            method: 'DELETE',
            body: JSON.stringify(ids),
        }).then(loadContacts())
            .catch(function(error) {
                console.log('cannot delete contacts');
            });
    });
}