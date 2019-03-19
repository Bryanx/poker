import { element, browser, by, Key } from 'protractor';

export class MenuPage {
  getPage() {
    return browser.get('/');
  }

  getPageTitle() {
    return browser.getTitle();
  }
}
