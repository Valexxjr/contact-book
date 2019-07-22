function validateContactForm() {
    const form = document.querySelector('#form_contact');
    const firstNameInput = form.querySelector('#first_name');
    const firstName = firstNameInput.value;
    if(firstName.length === 0 || firstName.length > 50) {
        firstNameInput.parentNode.classList.add('has-error');
        firstNameInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        firstNameInput.parentNode.classList.remove('has-error');
        firstNameInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const lastNameInput = form.querySelector('#last_name');
    const lastName = lastNameInput.value;
    if(lastName.length === 0 || lastName.length > 50) {
        lastNameInput.parentNode.classList.add('has-error');
        lastNameInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        lastNameInput.parentNode.classList.remove('has-error');
        lastNameInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const patronymicInput = form.querySelector('#patronymic');
    const patronymic = patronymicInput.value;
    if(patronymic.length > 50) {
        patronymicInput.parentNode.classList.add('has-error');
        patronymicInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        patronymicInput.parentNode.classList.remove('has-error');
        patronymicInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const birthdayDateInput = form.querySelector('#birthday_date');
    const birthdayDate = birthdayDateInput.value;
    if(birthdayDate) {
        if(!isValidDate(birthdayDate)) {
            birthdayDateInput.parentNode.classList.add('has-error');
            birthdayDateInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
            return false;
        }
        else {
            birthdayDateInput.parentNode.classList.remove('has-error');
            birthdayDateInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
        }
    }

    const citizenshipInput = form.querySelector('#citizenship');
    const citizenship = citizenshipInput.value;
    if(citizenship.length > 50 || /\d/.test(citizenship)) {
        citizenshipInput.parentNode.classList.add('has-error');
        citizenshipInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        citizenshipInput.parentNode.classList.remove('has-error');
        citizenshipInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const websiteInput = form.querySelector('#website');
    const website = websiteInput.value;
    if(website) {
        if (website.length > 50 || !website.includes('.')) {
            websiteInput.parentNode.classList.add('has-error');
            websiteInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
            return false;
        }
        else {
            websiteInput.parentNode.classList.remove('has-error');
            websiteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
        }
    }

    const emailInput = form.querySelector('#email');
    const email = emailInput.value;
    if(email) {
        if (email.length > 50 || !email.includes('@') || !email.includes('.')
            || email.startsWith('@') || email.startsWith('.') || email.endsWith('@') || email.endsWith('.')) {
            emailInput.parentNode.classList.add('has-error');
            emailInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
            return false;
        }
        else {
            emailInput.parentNode.classList.remove('has-error');
            emailInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
        }
    }

    const placeOfWorkInput = form.querySelector('#place_of_work');
    const placeOfWork = placeOfWorkInput.value;
    if(placeOfWork.length > 50) {
        placeOfWorkInput.parentNode.classList.add('has-error');
        placeOfWorkInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        placeOfWorkInput.parentNode.classList.remove('has-error');
        placeOfWorkInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const countryInput = form.querySelector('#country');
    const country = countryInput.value;
    if(country.length > 50 || /\d/.test(country)) {
        countryInput.parentNode.classList.add('has-error');
        countryInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        countryInput.parentNode.classList.remove('has-error');
        countryInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const cityInput = form.querySelector('#city');
    const city = cityInput.value;
    if(city.length > 50 || /\d/.test(city)) {
        cityInput.parentNode.classList.add('has-error');
        cityInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        cityInput.parentNode.classList.remove('has-error');
        cityInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const streetInput = form.querySelector('#street');
    const street = streetInput.value;
    if(street.length > 50) {
        streetInput.parentNode.classList.add('has-error');
        streetInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        streetInput.parentNode.classList.remove('has-error');
        streetInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const zipCodeInput = form.querySelector('#zip_code');
    const zipCode = zipCodeInput.value;
    if(zipCode.length > 10) {
        zipCodeInput.parentNode.classList.add('has-error');
        zipCodeInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        zipCodeInput.parentNode.classList.remove('has-error');
        zipCodeInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    return true;
}

function validateAttachmentForm() {
    const form = document.querySelector('#form_attachment');
    const fileInput = form.querySelector('#file');
    const id = form.querySelector('#id').value;
    if(!id){
        if(fileInput.files[0]) {
            fileInput.parentNode.classList.remove('has-error');
            fileInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
        }
        else {
            fileInput.parentNode.classList.add('has-error');
            fileInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
            return false;
        }
    }
    const noteInput = form.querySelector('#note');
    const note = noteInput.value;
    if(note) {
        if (note.length > 50) {
            noteInput.parentNode.classList.add('has-error');
            noteInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
            return false;
        }
        else {
            noteInput.parentNode.classList.remove('has-error');
            noteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
        }
    }
    else {
        noteInput.parentNode.classList.remove('has-error');
        noteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }
    return true;
}

function validatePhoneForm() {
    const form = document.querySelector('#form_phone');
    const countryCodeInput = form.querySelector('#country_code');
    const countryCode = countryCodeInput.value;
    if(countryCode.length === 0 || countryCode.length > 5) {
        countryCodeInput.parentNode.classList.add('has-error');
        countryCodeInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        countryCodeInput.parentNode.classList.remove('has-error');
        countryCodeInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const operatorCodeInput = form.querySelector('#operator_code');
    const operatorCode = operatorCodeInput.value;
    if(operatorCode.length === 0 || operatorCode.length > 5 || /\D/.test(operatorCode)) {
        operatorCodeInput.parentNode.classList.add('has-error');
        operatorCodeInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        operatorCodeInput.parentNode.classList.remove('has-error');
        operatorCodeInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    if(!form.querySelector('#radio_home').checked &&
        !form.querySelector('#radio_mobile').checked)
        return false;

    const numberInput = form.querySelector('#number');
    const number = numberInput.value;
    if(/\D/.test(number) || number.length === 0 || number.length > 10) {
        numberInput.parentNode.classList.add('has-error');
        numberInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        numberInput.parentNode.classList.remove('has-error');
        numberInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }

    const noteInput = form.querySelector('#note');
    const note = noteInput.value;
    if(note) {
        if (note.length > 50) {
            noteInput.parentNode.classList.add('has-error');
            noteInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
            return false;
        }
        else {
            noteInput.parentNode.classList.remove('has-error');
            noteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
        }
    }
    else {
        noteInput.parentNode.classList.remove('has-error');
        noteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }
    return true;
}

function validateEmails() {
    const form = document.querySelector('#form_email');
    const receiversInput = form.querySelector('#receivers');
    const receivers = receiversInput.value;
    if(receivers) {
        receiversInput.parentNode.classList.remove('has-error');
        receiversInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }
    else {
        receiversInput.parentNode.classList.add('has-error');
        receiversInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }

    const textInput = form.querySelector('#text');
    const text = textInput.value;
    if(!text) {
        textInput.parentNode.classList.add('has-error');
        textInput.parentNode.querySelectorAll('label')[1].classList.remove('d-none');
        return false;
    }
    else {
        textInput.parentNode.classList.remove('has-error');
        textInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
    }
    return true;
}


function isValidDate(dateString)
{
    if(!/^\d{4}-\d{1,2}-\d{1,2}$/.test(dateString))
        return false;

    var parts = dateString.split("-");
    var year = parseInt(parts[0], 10);
    var day = parseInt(parts[1], 10);
    var month = parseInt(parts[2], 10);


    if(year < 1000 || year > 3000 || month === 0 || month > 12)
        return false;

    var monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];

    if(year % 400 === 0 || (year % 100 !== 0 && year % 4 === 0))
        monthLength[1] = 29;

    return day > 0 && day <= monthLength[month - 1];
}