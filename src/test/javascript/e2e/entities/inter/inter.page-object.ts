import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class InterComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-inter div table .btn-danger'));
  title = element.all(by.css('jhi-inter div h2#page-heading span')).first();

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

export class InterUpdatePage {
  pageTitle = element(by.id('jhi-inter-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  ndInput = element(by.id('field_nd'));
  typeInterSelect = element(by.id('field_typeInter'));
  contractSelect = element(by.id('field_contract'));
  lastNameClientInput = element(by.id('field_lastNameClient'));
  firstNameClientInput = element(by.id('field_firstNameClient'));
  dateTimeInterInput = element(by.id('field_dateTimeInter'));
  complexInput = element(by.id('field_complex'));
  planningSelect = element(by.id('field_planning'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNdInput(nd) {
    await this.ndInput.sendKeys(nd);
  }

  async getNdInput() {
    return await this.ndInput.getAttribute('value');
  }

  async setTypeInterSelect(typeInter) {
    await this.typeInterSelect.sendKeys(typeInter);
  }

  async getTypeInterSelect() {
    return await this.typeInterSelect.element(by.css('option:checked')).getText();
  }

  async typeInterSelectLastOption(timeout?: number) {
    await this.typeInterSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setContractSelect(contract) {
    await this.contractSelect.sendKeys(contract);
  }

  async getContractSelect() {
    return await this.contractSelect.element(by.css('option:checked')).getText();
  }

  async contractSelectLastOption(timeout?: number) {
    await this.contractSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setLastNameClientInput(lastNameClient) {
    await this.lastNameClientInput.sendKeys(lastNameClient);
  }

  async getLastNameClientInput() {
    return await this.lastNameClientInput.getAttribute('value');
  }

  async setFirstNameClientInput(firstNameClient) {
    await this.firstNameClientInput.sendKeys(firstNameClient);
  }

  async getFirstNameClientInput() {
    return await this.firstNameClientInput.getAttribute('value');
  }

  async setDateTimeInterInput(dateTimeInter) {
    await this.dateTimeInterInput.sendKeys(dateTimeInter);
  }

  async getDateTimeInterInput() {
    return await this.dateTimeInterInput.getAttribute('value');
  }

  getComplexInput(timeout?: number) {
    return this.complexInput;
  }

  async planningSelectLastOption(timeout?: number) {
    await this.planningSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async planningSelectOption(option) {
    await this.planningSelect.sendKeys(option);
  }

  getPlanningSelect(): ElementFinder {
    return this.planningSelect;
  }

  async getPlanningSelectedOption() {
    return await this.planningSelect.element(by.css('option:checked')).getText();
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

export class InterDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-inter-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-inter'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
