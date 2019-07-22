function loadEmailForm(ids, emails) {
    setTab('mail');
    fetch('../resources/html/email_form.html?' + Math.random())
        .then(function (response) {
            return response.text()
        }).then(function (body) {
        document.querySelector('#main_content').innerHTML = body
    }).then(function () {
        const form = document.querySelector('#form_email');
        form.querySelector('#ids').value = ids.join(',');
        form.querySelector('#receivers').value = emails.join(', ');
        form.querySelector('#receivers').disabled = true;
        const select = form.querySelector('#template_select');
        const text = form.querySelector('#text');
        select.addEventListener('change', function () {
            switch(select.value) {
                case 'celebrate':
                    text.innerText = "Good day, $firstName$ $lastName$. \n " +
                        "We are going to celebrate our office anniversary and we want you to take part. " +
                        "Best regards, $placeOfWork$";
                    break;
                case 'news':
                    text.innerText = 'Добрый день, $firstName$ $lastName$. ' +
                        'Проверьте ваш почтовый ящик $email$ на наличие новостных сообщений';
                    break;
                case 'thanks':
                    text.innerText = 'We want to compliment one of your excellent employees, $firstName$ $lastName$. $placeOfWork$ went to great lengths to help us when our trouble occured.' +
                        'Such service builds great working relationships. We will bring all our needs to the $placeOfWork$ from now on.';
                    break;
                case 'info':
                    text.innerText = 'Some info: first name = $firstName$ \nlast name = $lastName$ \npatronymic = $patronymic$' +
                        ' \nbirthday date = $birthdayDate$ \n place of work $placeOfWork$ \n marital status = $maritalStatus$' +
                        ' \ncitizenship = $citizenship$';
                    break;
                default:
                    break;
            }
        });
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            const email = getEmailDetails();
            if(validateEmails()) {
                const data = fetch('/ContactBook/email', {
                    method: 'POST',
                    body: JSON.stringify(email)
                }).then(function (response) {
                    if(response.status === 200)
                    showSentMessage();
                    else
                        throw new Error("error while sending messages");
                }).catch(function(error) {
                    showNotSentMessage();
                });
            }
        });
        form.addEventListener('reset', function (event) {
            event.preventDefault();
            loadContacts();
        });
        document.querySelector('#sent_message .btn-warning').addEventListener('click', function () {
            hideSentMessage();
            loadContacts();
        }
        );
        document.querySelector('#not_sent_message .btn-warning').addEventListener('click', function () {
                hideNotSentMessage();
            }
        );
    })
}

function getEmailDetails() {
    const form = document.querySelector('#form_email');
    const receivers = form.querySelector('#ids').value;
    const title = form.querySelector('#title').value;
    const text = form.querySelector('#text').textContent;
    return {receivers: receivers, title: title, text: text};
}

function showSentMessage() {
    document.getElementById("sent_message").style.display = "block";
}

function hideSentMessage() {
    document.getElementById("sent_message").style.display = "none";
}

function showNotSentMessage() {
    document.getElementById("not_sent_message").style.display = "block";
}

function hideNotSentMessage() {
    document.getElementById("not_sent_message").style.display = "none";
}