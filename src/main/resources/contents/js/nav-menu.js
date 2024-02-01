window.onload = function () {
    const nav = document.getElementById('nav-wrapper');
    const hamburger = document.getElementById('js-hamburger');
    const blackBg = document.getElementById('js-black-back');

    hamburger.addEventListener('click', () => {
        nav.classList.toggle('open');
    });
    blackBg.addEventListener('click', () => {
        nav.classList.remove('open');
    });
}
