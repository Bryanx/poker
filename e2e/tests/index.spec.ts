import {by, browser, element, $, protractor, $$} from 'protractor';

describe('App E2E Test Suite', () => {
  beforeAll(() => {
    browser.get('/');
  });

  it('should navigate to login page', () => {
    // Testen of hij in de test methode komt.
    console.log('YAY');

    // Dit werkt niet
    const btnLogin = $('#login');
    btnLogin.click();

    // Dit werkt wel
    // browser.get('/login');
  });
});
