const msgRegister = document.getElementById('message-register');
const formRegister = document.getElementById('form-register');
const btnRegister = document.getElementById('btn-register');

const inputUsernameRegister = document.getElementById('input-username-register');
const inputEmailRegister = document.getElementById('input-email-register');
const inputPasswordRegister = document.getElementById('input-password-register');

btnRegister.addEventListener('click', clickedBtnRegister);

function clickedBtnRegister() {
    if ((inputUsernameRegister.value !== "")
        && (inputEmailRegister.value !== "")
        && (inputPasswordRegister.value !== "")) {
        const formDataRegister = new FormData(formRegister);
        const actionRegister = formRegister.getAttribute("action");
        const optionsRegister = {
            method: 'POST',
            body: formDataRegister,
        };

        fetch(actionRegister, optionsRegister).then((e)=> {
            if (e.status === 200) {
                window.location.assign("/signing");
            } else {
                msgRegister.style.display = "initial";
            }
        });
    }
}
