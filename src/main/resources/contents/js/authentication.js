const msgSigning = document.getElementById('message-signing');
const formSigning = document.getElementById('form-signing');
const btnSigning = document.getElementById('btn-signing');

const inputEmailSigning = document.getElementById('input-email-signing');
const inputPasswordSigning = document.getElementById('input-password-signing');

btnSigning.addEventListener('click', clickedBtnLogin);

function clickedBtnLogin() {
    if ((inputEmailSigning.value !== "")
        && (inputPasswordSigning.value !== "")) {
        const formDataSigning = new FormData(formSigning);
        const actionSigning = formSigning.getAttribute("action");
        const optionsSigning = {
            method: 'POST',
            body: formDataSigning,
        };

        fetch(actionSigning, optionsSigning).then((e)=> {
            if (e.status === 200) {
                window.location.assign("/home");
            } else {
                msgSigning.style.display = "initial";
            }
        });
    }
}
