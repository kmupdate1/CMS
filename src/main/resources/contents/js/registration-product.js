const msgRegister = document.getElementById('message-register');
const formRegister = document.getElementById('product-register');
const btnRegister = document.getElementById('btn-register');

const inputNameRegister = document.getElementById('name-register');

btnRegister.addEventListener('click', clickedBtnRegister);

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
                window.location.assign("/product/list/1");
            } else {
                msgRegister.style.display = "initial";
            }
        });
    }
}
