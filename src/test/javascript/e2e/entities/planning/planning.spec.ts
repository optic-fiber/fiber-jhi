/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PlanningComponentsPage, PlanningDeleteDialog, PlanningUpdatePage } from './planning.page-object';

const expect = chai.expect;

describe('Planning e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let planningUpdatePage: PlanningUpdatePage;
  let planningComponentsPage: PlanningComponentsPage;
  /*let planningDeleteDialog: PlanningDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Plannings', async () => {
    await navBarPage.goToEntity('planning');
    planningComponentsPage = new PlanningComponentsPage();
    await browser.wait(ec.visibilityOf(planningComponentsPage.title), 5000);
    expect(await planningComponentsPage.getTitle()).to.eq('fiberApp.planning.home.title');
  });

  it('should load create Planning page', async () => {
    await planningComponentsPage.clickOnCreateButton();
    planningUpdatePage = new PlanningUpdatePage();
    expect(await planningUpdatePage.getPageTitle()).to.eq('fiberApp.planning.home.createOrEditLabel');
    await planningUpdatePage.cancel();
  });

  /* it('should create and save Plannings', async () => {
        const nbButtonsBeforeCreate = await planningComponentsPage.countDeleteButtons();

        await planningComponentsPage.clickOnCreateButton();
        await promise.all([
            planningUpdatePage.setInitialTechInput('initialTech'),
            planningUpdatePage.setDateTimeCreationInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            planningUpdatePage.setLastNameTechInput('lastNameTech'),
            planningUpdatePage.setFirstNameTechInput('firstNameTech'),
            planningUpdatePage.userSelectLastOption(),
        ]);
        expect(await planningUpdatePage.getInitialTechInput()).to.eq('initialTech', 'Expected InitialTech value to be equals to initialTech');
        const selectedOpen = planningUpdatePage.getOpenInput();
        if (await selectedOpen.isSelected()) {
            await planningUpdatePage.getOpenInput().click();
            expect(await planningUpdatePage.getOpenInput().isSelected(), 'Expected open not to be selected').to.be.false;
        } else {
            await planningUpdatePage.getOpenInput().click();
            expect(await planningUpdatePage.getOpenInput().isSelected(), 'Expected open to be selected').to.be.true;
        }
        expect(await planningUpdatePage.getDateTimeCreationInput()).to.contain('2001-01-01T02:30', 'Expected dateTimeCreation value to be equals to 2000-12-31');
        expect(await planningUpdatePage.getLastNameTechInput()).to.eq('lastNameTech', 'Expected LastNameTech value to be equals to lastNameTech');
        expect(await planningUpdatePage.getFirstNameTechInput()).to.eq('firstNameTech', 'Expected FirstNameTech value to be equals to firstNameTech');
        await planningUpdatePage.save();
        expect(await planningUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await planningComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    });*/

  /* it('should delete last Planning', async () => {
        const nbButtonsBeforeDelete = await planningComponentsPage.countDeleteButtons();
        await planningComponentsPage.clickOnLastDeleteButton();

        planningDeleteDialog = new PlanningDeleteDialog();
        expect(await planningDeleteDialog.getDialogTitle())
            .to.eq('fiberApp.planning.delete.question');
        await planningDeleteDialog.clickOnConfirmButton();

        expect(await planningComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
