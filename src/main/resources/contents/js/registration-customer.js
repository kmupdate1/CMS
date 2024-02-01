const msgRegister = document.getElementById('message-register');
const msgFileRegister = document.getElementById('message-file-register');
const formRegister = document.getElementById('customer-register');
const formFileRegister = document.getElementById('customer-file-register');
const btnRegister = document.getElementById('btn-register');
const btnFileRegister = document.getElementById('btn-file-register');

const inputNameRegister = document.getElementById('name-register');
const inputFileRegister = document.getElementById('file-register');

btnRegister.addEventListener('click', clickedBtnRegister);
btnFileRegister.addEventListener('click', clickedBtnFileRegister)

function clickedBtnRegister() {
    if (inputNameRegister.value !== "") {
        const formDataRegister = new FormData(formRegister);
        const actionRegister = formRegister.getAttribute("action");
        const optionsRegister = {
            method: 'POST',
            body: formDataRegister,
        };

        fetch(actionRegister, optionsRegister).then((e) => {
            if (e.status === 200) {
                window.location.assign("/customer/list/1");
            } else {
                msgRegister.style.display = "initial";
            }
        });
    }
}

function clickedBtnFileRegister() {
    if (inputFileRegister.value !== null) {
        const formDataFileRegister = new FormData(formFileRegister);
        const actionFileRegister = formFileRegister.getAttribute("action");
        const optionFileRegister = {
            method: 'POST',
            body: formDataFileRegister,
        };

        fetch(actionFileRegister, optionFileRegister).then((e) => {
            if (e.status === 200) {
                window.location.assign("/customer/list/1");
            } else {
                msgFileRegister.style.display = 'initial';
            }
        });
    }
}
