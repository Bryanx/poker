import { by, browser, element } from 'protractor';

describe('Conduit App E2E Test Suite', () => {
  let page;

  describe('home page should work fine', () => {
    beforeAll(() => {
      page = browser.get('/');
    });

    it('should have right title', () => {
      console.log('helloooo' + page.getTitle());
      page.getTitle()
        .then((title: string) => {
          expect(title).toEqual('Conduit');
        });
    });
  });
});
