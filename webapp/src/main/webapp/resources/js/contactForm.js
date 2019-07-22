function loadContactFormForCreate() {
    setTab('edit');

    const form = fetch('../resources/html/form.html?' + Math.random()).then(function(response) {
        return response.text()
    });
    const phoneTemplate = fetch('../resources/html/phone_table.html?' + Math.random()).then(function(response) {
        return response.text()
    });
    const attachmentTemplate = fetch('../resources/html/attachment_table.html?' + Math.random()).then(function(response) {
        return response.text()
    });

    Promise.all([phoneTemplate, attachmentTemplate, form])
        .then(function(response) {
            const phoneTemplate = response[0];
            const attachmentTemplate = response[1];
            const formTemplate = response[2];

            document.getElementById('main_content').innerHTML = formTemplate;

            document.getElementById('main_content').innerHTML += mustacheRender(phoneTemplate, "");
            document.getElementById('main_content').innerHTML += mustacheRender(attachmentTemplate, "");

            document.querySelector('#contact_image').src = '../files/avatar.png';
            addContactFormPageListeners();

            document.querySelector('#form_contact').addEventListener('submit', function (event) {
                event.preventDefault();
                if(validateContactForm()) {
                    sendNewContact(getContactData());
                }
            });

        }).catch(function(error) {
        console.log('Unable to get all template data: ', error.message)
    });

}

function addContactFormPageListeners() {
    document.querySelector('#add_phone').addEventListener('click', function () {
        clearPhoneForm();
        showPhoneForm();
    });
    document.querySelector('#form_phone').addEventListener('submit', function (event) {
        event.preventDefault();
        if(validatePhoneForm()) {
            addPhoneToTable(phoneFromForm());
            hidePhoneForm();
        }
    });

    document.querySelector('#add_attachment').addEventListener('click', function () {
        clearAttachmentForm();
        showAttachmentForm();
    });

    document.querySelector('#form_attachment').addEventListener('submit', function (event) {
        event.preventDefault();
        if(validateAttachmentForm()) {
            addAttachmentToTable(attachmentFromForm());
            hideAttachmentForm();
        }
    });

    document.querySelector('#form_image').addEventListener('submit', function (event) {
        event.preventDefault();
        const contactImage = document.querySelector('#contact_image');
        const file = document.querySelector('#image_file').files[0];
        const reader  = new FileReader();

        reader.onloadend = function () {
            contactImage.src = reader.result;
        }

        if (file) {
            reader.readAsDataURL(file);
        } else {
            contactImage.src = "../files/avatar.png";
        }
        hideImageForm();
    });

    document.querySelector('#form_image').addEventListener('reset', function (event) {
        event.preventDefault();
        document.querySelector('#image_file').files = null;
        hideImageForm();
    });

    var btnDelete = document.querySelector('#delete_phone');
    btnDelete.disabled = true;
    var phoneTable = document.querySelector('#table_phone');
    btnDelete.addEventListener('click', deleteFromTable.bind(null, phoneTable, btnDelete));

    btnDelete = document.querySelector('#delete_attachment');
    btnDelete.disabled = true;
    var attachmentTable = document.querySelector('#table_attachment');
    btnDelete.addEventListener('click', deleteFromTable.bind(null, attachmentTable, btnDelete));
}


function loadContactFormForEdit(contactId) {
    setTab('edit');

    const contactUrl = '/ContactBook/contacts/' + contactId;
    const form = fetch('../resources/html/form.html?' + Math.random()).then(function(response) {
        return response.text()
    });
    const contactPhones = fetch(contactUrl + '/phones').then(function(response) {
        return response.json()
    });
    const contactAttachments = fetch(contactUrl + '/attachments').then(function(response) {
        return response.json()
    });
    const phoneTemplate = fetch('../resources/html/phone_table.html?' + Math.random()).then(function(response) {
        return response.text()
    });
    const attachmentTemplate = fetch('../resources/html/attachment_table.html?' + Math.random()).then(function(response) {
        return response.text()
    });
    const contact = fetch(contactUrl).then(function(response) {
        return response.json()
    });

    Promise.all([contactPhones, contactAttachments, phoneTemplate, attachmentTemplate, contact, form])
        .then(function(response) {
            const phoneData = response[0];
            const attachmentData = response[1];
            const phoneTemplate = response[2];
            const attachmentTemplate = response[3];
            const contact = response[4];
            const formTemplate = response[5];

            document.getElementById('main_content').innerHTML = formTemplate;
            if(contact.image)
                document.querySelector('#contact_image').src = '../files/contacts/' + contact.id + '/' + contact.image;
            else
                document.querySelector('#contact_image').src = '../files/avatar.png';
            document.getElementById('main_content').innerHTML += mustacheRender(phoneTemplate, phoneData);
            document.getElementById('main_content').innerHTML += mustacheRender(attachmentTemplate, attachmentData);
            setContact(contact);

            addContactFormPageListeners();

            var phones = document.querySelectorAll('#table_phone > tbody > tr');
            phones.forEach(function (row) {
                var btnEdit = row.querySelector('.edit');
                if (btnEdit) {
                    var phone = {
                        id : row.querySelector('.d-none').textContent,
                        countryCode : row.querySelector('.country_code').textContent,
                        operatorCode : row.querySelector('.operator_code').textContent,
                        number : row.querySelector('.number').textContent,
                        numberType : row.querySelector('.number_type').textContent,
                        note : row.querySelector('.note').textContent
                    };
                    btnEdit.addEventListener('click', function () {
                        setPhoneForm(phone);
                        showPhoneForm();
                    }, false);
                }
            });
            var attachments = document.querySelectorAll('#table_attachment > tbody > tr');

            attachments.forEach(function (row) {
                const a = row.querySelector('a');
                a.href = '../files/contacts/' + contact.id + '/attachments/' + row.querySelector('.d-none').textContent + '/';// + a.textContent;
                const btnEdit = row.querySelector('.edit');
                if (btnEdit) {
                    var attachment = {
                        id : row.querySelector('.d-none').textContent,
                        file : row.querySelector('.file').firstChild.files[0],
                        note : row.querySelector('.note').textContent
                    };
                    btnEdit.addEventListener('click', function () {
                        setAttachmentForm(attachment);
                        showAttachmentForm();
                    }, false);
                }
            });
            document.querySelector('#form_contact').addEventListener('submit', function (event) {
                event.preventDefault();
                if(validateContactForm()) {
                    sendContact(getContactData());
                }
            });
            var btnDelete = document.querySelector('#delete_phone');
            var phoneTable = document.querySelector('#table_phone');
            var checkboxes = phoneTable.querySelectorAll('input.checkbox');
            checkboxes.forEach(function(checkbox) {
                checkbox.addEventListener('change', checkboxChanged.bind(null, phoneTable, btnDelete))
            });

            btnDelete = document.querySelector('#delete_attachment');
            //btnDelete.disabled = true;
            var attachmentTable = document.querySelector('#table_attachment');
            checkboxes = attachmentTable.querySelectorAll('input.checkbox');
            checkboxes.forEach(function(checkbox) {
                checkbox.addEventListener('change', checkboxChanged.bind(null, attachmentTable, btnDelete))
            });

        }).catch(function(error) {
        console.log('Unable to get all template data: ', error.message)
    });


}

function phoneFromRow(row) {
    const id = row.querySelector('.d-none').textContent;
    const countryCode = row.querySelector('.country_code').textContent;
    const operatorCode = row.querySelector('.operator_code').textContent;
    const numberType = row.querySelector('.number_type').textContent;
    const number = row.querySelector('.number').textContent;
    const note = row.querySelector('.note').textContent;

    return {id: id, countryCode : countryCode, operatorCode : operatorCode,
        number : number, numberType : numberType, note : note};
}

function attachmentFromRow(row) {
    const id = row.querySelector('.d-none').textContent;
    const filename = row.querySelector('.filename').textContent;
    const file = row.querySelector('.file > input').files[0];
    const uploadDate = row.querySelector('.upload_date').textContent;
    const note = row.querySelector('.note').textContent;

    return {id: id, filename: filename, file: file, uploadDate: uploadDate, note : note};
}

function sendNewContact(contact) {

    const image = document.querySelector('#image_file').files[0];
    if(image)
        contact.image = image.name;
    else
        contact.image = null;

    let formData = new FormData();

    const phones = Array.from(document.querySelectorAll('#table_phone > tbody > tr'));
    const attachments = Array.from(document.querySelectorAll('#table_attachment > tbody > tr'));

    const phonesToInsert = phones.filter(function(phone) {
        return phone.classList.contains('created')
            && !phone.classList.contains('deleted')
    }).map(function(row) {
        return phoneFromRow(row);
    });

    const attachmentsToInsert = attachments.filter(function(attachment) {
        return attachment.classList.contains('created')
            && !attachment.classList.contains('deleted')
    }).map(function(row) {
        return attachmentFromRow(row);
    });

    formData.append('image', image);
    formData.append("contact", JSON.stringify(contact));


    phonesToInsert.forEach(function(phone) {
        phone.id = null
    });

    formData.append('phonesInsert', JSON.stringify(phonesToInsert));

    let formDataAttachmentIds = 1;

    attachmentsToInsert.forEach(function (attachment) {
        formData.append('insert:' + formDataAttachmentIds++, attachment.file);
        attachment.id = null;
        delete attachment.file;
    });
    formData.append('attachmentsInsert', JSON.stringify(attachmentsToInsert));

    fetch('/ContactBook/contacts', {
        method: 'POST',
        body: formData,
    }).then(loadContacts());
}

function sendContact(contact) {

    const image = document.querySelector('#image_file').files[0];
    if(image)
        contact.image = image.name;
    else
        contact.image = null;

    let formData = new FormData();

    const phones = Array.from(document.querySelectorAll('#table_phone > tbody > tr'));
    const attachments = Array.from(document.querySelectorAll('#table_attachment > tbody > tr'));

    const phonesToUpdate = phones.filter(function(phone) {
        return phone.classList.contains('updated')
            && !phone.classList.contains('created')
            && !phone.classList.contains('deleted')
    }).map(function(row) {
        return phoneFromRow(row);
    });

    const phonesToDelete = phones.filter(function(phone) {
        return phone.classList.contains('deleted')
            && !phone.classList.contains('created')
    }).map(function(row) {
        return phoneFromRow(row)
    });

    const phonesToInsert = phones.filter(function(phone) {
        return phone.classList.contains('created')
            && !phone.classList.contains('deleted')
    }).map(function(row) {
        return phoneFromRow(row)
    });

    const attachmentsToUpdate = attachments.filter(function(attachment) {
        return attachment.classList.contains('updated')
            && !attachment.classList.contains('created')
            && !attachment.classList.contains('deleted')
    }).map(function(row) {
        return attachmentFromRow(row)
    });

    const attachmentsToDelete = attachments.filter(function(attachment) {
        return attachment.classList.contains('deleted')
            && !attachment.classList.contains('created')
    }).map(function(row) {
        return attachmentFromRow(row)
    });

    const attachmentsToInsert = attachments.filter(function(attachment) {
        return attachment.classList.contains('created')
            && !attachment.classList.contains('deleted')
    }).map(function(row) {
        return attachmentFromRow(row)
    });

    console.log(JSON.stringify(contact));

    formData.append('image', image);
    formData.append("contact", JSON.stringify(contact));


    phonesToInsert.forEach(function(phone) {
        phone.id = null
    });

    formData.append('phonesInsert', JSON.stringify(phonesToInsert));
    formData.append('phonesUpdate', JSON.stringify(phonesToUpdate));
    formData.append('phonesDelete', JSON.stringify(phonesToDelete));

    let formDataAttachmentIds = 1;

    attachmentsToInsert.forEach(function (attachment) {
        formData.append('insert:' + formDataAttachmentIds++, attachment.file);
        attachment.id = null;
        delete attachment.file;
    });
    formData.append('attachmentsInsert', JSON.stringify(attachmentsToInsert));

    attachmentsToUpdate.forEach(function (attachment) {
        if(attachment.file) {
            formData.append('update:' + attachment.id, attachment.file);
            delete attachment.file;
        } else {
            // marks that file not will be updated
            attachment.filename = null;
        }
    });
    formData.append('attachmentsUpdate', JSON.stringify(attachmentsToUpdate));
    formData.append('attachmentsDelete', JSON.stringify(attachmentsToDelete));

    fetch('/ContactBook/contacts/' + contact.id, {
        method: 'PUT',
        body: formData,
    }).then(loadContacts());
}

function deleteFromTable(table, btnDelete) {
    var body = table.querySelector('tbody');
    var rows = body.childNodes;

    rows.forEach(function (row) {
        if(row.nodeType === Node.ELEMENT_NODE) {
            var checkbox = row.querySelector('td > input[type=checkbox]');
            if(checkbox)
                if(checkbox.checked) {
                    row.classList.add('deleted');
                    row.querySelector('input.checkbox').checked = false;
                    checkboxChanged(table, btnDelete);
                }
        }
    })
}

function addAttachmentToTable(attachment) {
    const table = document.querySelector('#table_attachment');
    const btnDelete = document.querySelector('#delete_attachment');
    const body = table.querySelector('tbody');
    const rows = body.querySelectorAll('tr');
    let newRow = true;
    rows.forEach(function (row) {
        if(row.querySelector('.d-none').textContent === attachment.id) {
            if(attachment.file[0]) {
                row.querySelector('.filename').textContent = attachment.file[0].name;
                row.querySelector('.file > input').files = attachment.file;
                row.querySelector('.upload_date').textContent = attachment.uploadDate;
            }
            row.querySelector('.note').textContent = attachment.note;
            row.classList.add('updated');
            newRow = false;
        }
    });
    if(newRow) {
        var row = document.createElement('tr');
        row.innerHTML = '<td><input class="checkbox" type="checkbox"></td>' +
            '<td class="d-none">' + attachment.id + '</td>' +
            '<td class="filename">' + attachment.file[0].name + '</td>' +
            '<td class="d-none file"><input type="file"></td>' +
            '<td class="upload_date">' + attachment.uploadDate + '</td>' +
            '<td class="note">' + attachment.note + '</td>' +
            '<td class="edit"><button>Edit</button></td>';
        row.querySelector('.file > input').files = attachment.file;
        row.classList.add('created');
        body.appendChild(row);
        const checkbox = row.querySelector('input.checkbox');
        checkbox.addEventListener('change', checkboxChanged.bind(null, table, btnDelete));
        const btnEdit = row.querySelector('.edit');
        btnEdit.addEventListener('click', function () {
            setAttachmentForm(attachment);
            showAttachmentForm();
        }, false);
    }
}


function addPhoneToTable(phone) {
    const table = document.querySelector('#table_phone');
    const btnDelete = document.querySelector('#delete_phone');
    const body = table.querySelector('tbody');
    const rows = body.querySelectorAll('tr');
    let newRow = true;
    rows.forEach(function (row) {
        if(row.querySelector('.d-none').textContent === phone.id) {
            row.querySelector('.country_code').textContent = phone.countryCode;
            row.querySelector('.operator_code').textContent = phone.operatorCode;
            row.querySelector('.country_code').textContent = phone.countryCode;
            row.querySelector('.number').textContent = phone.number;
            row.querySelector('.number_type').textContent = phone.numberType;
            row.querySelector('.note').textContent = phone.note;
            row.classList.add('updated');
            newRow = false;
        }
    });
    if(newRow) {
        var row = document.createElement('tr');
        row.innerHTML = '<td><input class="checkbox" type="checkbox"></td>\n' +
            '<td class="d-none">' + phone.id + '</td>\n' +
            '<td class="country_code">' + phone.countryCode + '</td>\n' +
            '<td class="operator_code">' + phone.operatorCode + '</td>\n' +
            '<td class="number">' + phone.number + '</td>\n' +
            '<td class="number_type">' + phone.numberType + '</td>\n' +
            '<td class="note">' + phone.note + '</td>\n' +
            '<td class="edit"><button>Edit</button> </td>';
        row.classList.add('created');
        body.appendChild(row);
        const checkbox = row.querySelector('input.checkbox');
        checkbox.addEventListener('change', checkboxChanged.bind(null, table, btnDelete));
        var btnEdit = row.querySelector('.edit');
        btnEdit.addEventListener('click', function () {
            setPhoneForm(phone);
            showPhoneForm();
        }, false);
    }
}

function attachmentFromForm() {
    const form = document.querySelector('#form_attachment');
    let id = form.querySelector('#id').value;
    const file = form.querySelector('#file').files;
    const date = new Date();
    const uploadDate = date.toISOString().slice(0, 10);
    const note = form.querySelector('#note').value;
    if(id === '')
        id = file[0].name;

    return {id: id, file: file, uploadDate: uploadDate, note : note};
}

function phoneFromForm() {
    var form = document.querySelector('#form_phone');
    let id = form.querySelector('#id').value;
    var countryCode = form.querySelector('#country_code').value;
    var operatorCode = form.querySelector('#operator_code').value;
    var numberType;
    if(form.querySelector('#radio_home').checked)
        numberType = 'HOME';
    if(form.querySelector('#radio_mobile').checked)
        numberType = 'MOBILE';
    var number = form.querySelector('#number').value;
    var note = form.querySelector('#note').value;
    if(id === '')
        id = countryCode + operatorCode + number;

    return {id: id, countryCode : countryCode, operatorCode : operatorCode,
        number : number, numberType : numberType, note : note};
}


function clearPhoneForm() {
    var emptyPhone = {id: '', countryCode: '', operatorCode: '',
        numberType: 'MOBILE', number: '', note: ''};
    setPhoneForm(emptyPhone);
    const form = document.querySelector('#form_phone');

    const countryCodeInput = form.querySelector('#country_code');
    countryCodeInput.parentNode.classList.remove('has-error');
    countryCodeInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');

    const operatorCodeInput = form.querySelector('#operator_code');
    operatorCodeInput.parentNode.classList.remove('has-error');
    operatorCodeInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');

    const numberInput = form.querySelector('#number');
    numberInput.parentNode.classList.remove('has-error');
    numberInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');

    const noteInput = form.querySelector('#note');
    noteInput.parentNode.classList.remove('has-error');
    noteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
}

function clearAttachmentForm() {
    var emptyAttachment = {id: '', file: null, note: ''};
    setAttachmentForm(emptyAttachment);
    const form = document.querySelector('#form_attachment');
    const fileInput = form.querySelector('#file');
    fileInput.parentNode.classList.remove('has-error');
    fileInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');

    const noteInput = form.querySelector('#note');
    const note = noteInput.value;
    noteInput.parentNode.classList.remove('has-error');
    noteInput.parentNode.querySelectorAll('label')[1].classList.add('d-none');
}

function setAttachmentForm(attachment) {
    var form = document.querySelector('#form_attachment');
    form.querySelector('#id').value = attachment.id;
    form.querySelector('#file').files = attachment.file;

    form.querySelector('#note').value = attachment.note;
}

function setPhoneForm(phone) {
    var form = document.querySelector('#form_phone');
    form.querySelector('#id').value = phone.id;
    form.querySelector('#country_code').value = phone.countryCode;
    form.querySelector('#operator_code').value = phone.operatorCode;

    if(phone.numberType === 'HOME')
        form.querySelector('#radio_home').checked = true;

    if(phone.numberType === 'MOBILE')
        form.querySelector('#radio_mobile').checked = true;

    form.querySelector('#number').value = phone.number;
    form.querySelector('#note').value = phone.note;
}

function setContact(contact) {
    document.querySelector('#contact_id').value = contact.id;
    document.querySelector('#first_name').value = contact.firstName;
    document.querySelector('#last_name').value = contact.lastName;
    document.querySelector('#patronymic').value = contact.patronymic;
    document.querySelector('#birthday_date').value = contact.birthdayDate;
    document.querySelector('#citizenship').value = contact.citizenship;
    if(contact.gender === "MALE")
        document.querySelector('#radio_male').checked = true;
    if(contact.gender === "FEMALE")
        document.querySelector('#radio_female').checked = true;
    const maritalStatus = contact.maritalStatus;
    document.querySelector('#marital_status_select').value =
        (maritalStatus === null)?(null):maritalStatus.toLowerCase();
    document.querySelector('#website').value = contact.website;
    document.querySelector('#email').value = contact.email;
    document.querySelector('#place_of_work').value = contact.placeOfWork;
    document.querySelector('#country').value = contact.country;
    document.querySelector('#city').value = contact.city;
    document.querySelector('#street').value = contact.street;
    document.querySelector('#zip_code').value = contact.zipCode;
}

function showPhoneForm() {
    document.getElementById("form_phone").style.display = "block";
}

function hidePhoneForm() {
    document.getElementById("form_phone").style.display = "none";
}

function showAttachmentForm() {
    document.getElementById("form_attachment").style.display = "block";
}

function hideAttachmentForm() {
    document.getElementById("form_attachment").style.display = "none";
}

function showImageForm() {
    document.getElementById("form_image").style.display = "block";
}

function hideImageForm() {
    document.getElementById("form_image").style.display = "none";
}

function getContactData() {
    const id = document.querySelector('#contact_id').value;
    const firstName = document.querySelector('#first_name').value;
    const lastName = document.querySelector('#last_name').value;
    let patronymic = document.querySelector('#patronymic').value;
    if(patronymic === '')
        patronymic = null;
    let birthdayDate = document.querySelector('#birthday_date').value;
    if(birthdayDate === '')
        birthdayDate = null;
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
    let website = document.querySelector('#website').value;
    if(website === '')
        website = null;
    let email = document.querySelector('#email').value;
    if(email === '')
        email = null;
    let placeOfWork = document.querySelector('#place_of_work').value;
    if(placeOfWork === '')
        placeOfWork = null;
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
    let contact = {id: id, firstName: firstName,
        lastName: lastName, patronymic: patronymic,
        birthdayDate: birthdayDate, gender: gender,
        citizenship: citizenship, maritalStatus: maritalStatus,
        website: website, email: email, placeOfWork: placeOfWork,
        country: country, city: city, street: street, zipCode: zipCode
    };

    return contact;
}