const btnSendEmail = document.getElementById('btn-send-email');
const formVerification = document.getElementById('form-verification');
const inputEmailVerification = document.getElementById('input-email-verification');
const verification = document.getElementById('verification');
const accountExists = document.getElementById('account-exist');
const accountNotExists = document.getElementById('account-not-exists');
const verificationLink = document.getElementById('verification-link');
const displaySec = document.getElementById('display-sec');

let count = 5;
let intervalId = null;

btnSendEmail.addEventListener('click', clickedBtnReset);
verificationLink.addEventListener('click', () => {
    // wrapper.classList.remove('active');
    verification.style.display = 'initial';
    accountExists.style.display = 'none';
    accountNotExists.style.display = 'none';
});

// 送信ボタンが発火したときの処理
function clickedBtnReset() {
    if (inputEmailVerification.value !== ""){
        const verificationFormData = new FormData(formVerification);
        const actionVerification = formVerification.getAttribute("action");
        const optionsVerification = {
            method: 'POST',
            body: verificationFormData,
        };

        fetch(actionVerification, optionsVerification).then((e)=> {
            if (e.status === 200) {
                verification.style.display = 'none';
                accountExists.style.display = "initial";
                accountNotExists.style.display = "none";
                intervalId = setInterval(countDown, 1000);
            } else {
                verification.style.display = 'none';
                accountExists.style.display = "none";
                accountNotExists.style.display = "initial";
            }
        });
    }
}

// 一定時間おきに行いたい関数を宣言
function countDown() {
    if(count >= 0) {
        displaySec.textContent = count.toString();
    } else {
        clearInterval(intervalId);
        window.location.assign("/signing");
    }
    count--;
}
