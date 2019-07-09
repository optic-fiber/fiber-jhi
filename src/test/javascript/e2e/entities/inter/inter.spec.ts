/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InterComponentsPage, InterDeleteDialog, InterUpdatePage } from './inter.page-object';

const expect = chai.expect;

describe('Inter e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let interUpdatePage: InterUpdatePage;
  let interComponentsPage: InterComponentsPage;
  /*let interDeleteDialog: InterDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Inters', async () => {
    await navBarPage.goToEntity('inter');
    interComponentsPage = new InterComponentsPage();
    await browser.wait(ec.visibilityOf(interComponentsPage.title), 5000);
    expect(await interComponentsPage.getTitle()).to.eq('fiberApp.inter.home.title');
  });

  it('should load create Inter page', async () => {
    await interComponentsPage.clickOnCreateButton();
    interUpdatePage = new InterUpdatePage();
    expect(await interUpdatePage.getPageTitle()).to.eq('fiberApp.inter.home.createOrEditLabel');
    await interUpdatePage.cancel();
  });

  /* it('should create and save Inters', async () => {
        const nbButtonsBeforeCreate = await interComponentsPage.countDeleteButtons();

        await interComponentsPage.clickOnCreateButton();
        await promise.all([
            interUpdatePage.setNdInput('nd'),
            interUpdatePage.typeInterSelectLastOption(),
            interUpdatePage.contractSelectLastOption(),
            interUpdatePage.setLastNameClientInput('lastNameClient'),
            interUpdatePage.setFirstNameClientInput('firstNameClient'),
            interUpdatePage.setDateTimeInterInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            interUpdatePage.planningSelectLastOption(),
        ]);
        expect(await interUpdatePage.getNdInput()).to.eq('nd', 'Expected Nd value to be equals to nd');
        expect(await interUpdatePage.getLastNameClientInput()).to.eq('lastNameClient', 'Expected LastNameClient value to be equals to lastNameClient');
        expect(await interUpdatePage.getFirstNameClientInput()).to.eq('firstNameClient', 'Expected FirstNameClient value to be equals to firstNameClient');
        expect(await interUpdatePage.getDateTimeInterInput()).to.contain('2001-01-01T02:30', 'Expected dateTimeInter value to be equals to 2000-12-31');
        const selectedComplex = interUpdatePage.getComplexInput();
        if (await selectedComplex.isSelected()) {
            await interUpdatePage.getComplexInput().click();
            expect(await interUpdatePage.getComplexInput().isSelected(), 'Expected complex not to be selected').to.be.false;
        } else {
            await interUpdatePage.getComplexInput().click();
            expect(await interUpdatePage.getComplexInput().isSelected(), 'Expected complex to be selected').to.be.true;
        }
        await interUpdatePage.save();
        expect(await interUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await interComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    });*/

  /* it('should delete last Inter', async () => {
        const nbButtonsBeforeDelete = await interComponentsPage.countDeleteButtons();
        await interComponentsPage.clickOnLastDeleteButton();

        interDeleteDialog = new InterDeleteDialog();
        expect(await interDeleteDialog.getDialogTitle())
            .to.eq('fiberApp.inter.delete.question');
        await interDeleteDialog.clickOnConfirmButton();

        expect(await interComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
