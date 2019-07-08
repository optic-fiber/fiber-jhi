import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class PlanningComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-planning div table .btn-danger'));
  title = element.all(by.css('jhi-planning div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class PlanningUpdatePage {
  pageTitle = element(by.id('jhi-planning-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  initialTechInput = element(by.id('field_initialTech'));
  openInput = element(by.id('field_open'));
  dateTimeCreationInput = element(by.id('field_dateTimeCreation'));
  lastNameTechInput = element(by.id('field_lastNameTech'));
  firstNameTechInput = element(by.id('field_firstNameTech'));
  userSelect = element(by.id('field_user'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setInitialTechInput(initialTech) {
    await this.initialTechInput.sendKeys(initialTech);
  }

  async getInitialTechInput() {
    return await this.initialTechInput.getAttribute('value');
  }

  getOpenInput(timeout?: number) {
    return this.openInput;
  }
  async setDateTimeCreationInput(dateTimeCreation) {
    await this.dateTimeCreationInput.sendKeys(dateTimeCreation);
  }

  async getDateTimeCreationInput() {
    return await this.dateTimeCreationInput.getAttribute('value');
  }

  async setLastNameTechInput(lastNameTech) {
    await this.lastNameTechInput.sendKeys(lastNameTech);
  }

  async getLastNameTechInput() {
    return await this.lastNameTechInput.getAttribute('value');
  }

  async setFirstNameTechInput(firstNameTech) {
    await this.firstNameTechInput.sendKeys(firstNameTech);
  }

  async getFirstNameTechInput() {
    return await this.firstNameTechInput.getAttribute('value');
  }

  async userSelectLastOption(timeout?: number) {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option) {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption() {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PlanningDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-planning-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-planning'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
