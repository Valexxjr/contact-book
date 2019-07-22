function loadSearchForm() {
    setTab('search');
    fetch('../resources/html/search_form.html?' + Math.random())
        .then(function (response) {
            return response.text()
        }).then(function (body) {
        document.querySelector('#main_content').innerHTML = body
    }).then(function () {
        const form = document.querySelector('#form_search');
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            loadContactsSearch();
        })
    });
}

function getSearchParameters() {
    let firstName = document.querySelector('#first_name').value;
    if(firstName === '')
        firstName = null;
    let lastName = document.querySelector('#last_name').value;
    if(lastName === '')
        lastName = null;
    let patronymic = document.querySelector('#patronymic').value;
    if(patronymic === '')
        patronymic = null;
    let dateFrom = document.querySelector('#date_left').value;
    if(dateFrom === '')
        dateFrom = null;
    let dateTo = document.querySelector('#date_right').value;
    if(dateTo === '')
        dateTo = null;
    let citizenship = document.querySelector('#citizenship').value;
    if(citizenship === '')
        citizenship = null;
    let maritalStatus = document.querySelector('#marital_status_select').value.toUpperCase();
    if(maritalStatus === '')
        maritalStatus = null;
    let gender = null;
    if(document.querySelector('#radio_male').checked)
        gender = "MALE";
    if(document.querySelector('#radio_female').checked)
        gender = "FEMALE";
    let country = document.querySelector('#country').value;
    if(country === '')
        country = null;
    let city = document.querySelector('#city').value;
    if(city === '')
        city = null;
    let street = document.querySelector('#street').value;
    if(street === '')
        street = null;
    let zipCode = document.querySelector('#zip_code').value;
    if(zipCode === '')
        zipCode = null;

    return {firstName: firstName,
        lastName: lastName, patronymic: patronymic,
        dateFrom: dateFrom, dateTo: dateTo, gender: gender,
        citizenship: citizenship, maritalStatus: maritalStatus,
        country: country, city: city, street: street, zipCode: zipCode
    };
}