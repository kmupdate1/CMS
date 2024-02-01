const msgRegister = document.getElementById('message-register');
const formRegister = document.getElementById('info-form-register');
const btnRegister = document.getElementById('btn-register');

const inputIdentifierRegister = document.getElementById('identifier-register');
const inputPasswordRegister = document.getElementById('password-register');
const inputUsernameRegister = document.getElementById('name-register');

btnRegister.addEventListener('click', clickedBtnRegister);

function clickedBtnRegister() {
    if ((inputUsernameRegister.value !== "")
        && (inputIdentifierRegister.value !== "")
        && (inputPasswordRegister.value !== "")) {
        const formDataRegister = new FormData(formRegister);
        const actionRegister = formRegister.getAttribute("action");
        const optionsRegister = {
            method: 'POST',
            body: formDataRegister,
        };

        fetch(actionRegister, optionsRegister).then((e)=> {
            if (e.status === 200) {
                window.location.assign("/setting/account/list/1");
            } else {
                msgRegister.style.display = "initial";
            }
        });
    }
}
