import {by, browser, element, $, protractor, $$} from 'protractor';
import {MenuPage} from '../pages/menu.page';

describe('App E2E Test Suite', () => {
  const menuPage = new MenuPage();

  beforeAll(() => {
    browser.get('/');
  });

  it('should navigate to login page', () => {
    browser.get('/login');
  });
});
