import {by, browser, element, $, protractor, $$} from 'protractor';

describe('App E2E Test Suite', () => {

  beforeAll(() => {
    browser.waitForAngularEnabled(false);
    browser.get('/login');
  });

  it('h1 tag should be visible',  () => {
    expect(element(by.tagName('h1'))).toBeDefined();
  });

  it('h1 tag should contain text1',  () => {
    expect(element(by.tagName('h1')).getText()).toBeDefined();
  });

  it('should be able to get username',  () => {
    expect(element(by.css('#username')).getText()).toBeDefined();
  });

  it('should be able to get password',  () => {
    expect(element(by.css('#password')).getText()).toBeDefined();
  });

  it('should be able to change username',  async () => {
    const username = element(by.css('#username'));
    await username.sendKeys('remismeets');
    expect(username.getAttribute('value')).toEqual('remismeets');
  });

  it('should be able to login',  async () => {
    const username = element(by.css('#username'));
    const password = element(by.css('#password'));
    const submit = element(by.css('.login-button'));
    username.click();
    username.clear();
    username.sendKeys('remismeets');
    password.click();
    password.clear();
    password.sendKeys('12345');
    submit.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toEqual(browser.baseUrl + '/');
  });
});
