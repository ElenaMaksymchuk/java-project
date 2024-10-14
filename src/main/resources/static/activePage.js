document.addEventListener('DOMContentLoaded', (event) => {
    const currentLocation = window.location.pathname;
    const headerMenuLinks = document.querySelectorAll('.headerMenu a');
    headerMenuLinks.forEach((link) => {
        if (link.pathname === currentLocation) {
            link.classList.add('active');
        }
    });
    const footerMenuLinks = document.querySelectorAll('.footerMenu a');
    footerMenuLinks.forEach((link) => {
        if (link.pathname === currentLocation) {
            link.classList.add('active');
        }
    });
});