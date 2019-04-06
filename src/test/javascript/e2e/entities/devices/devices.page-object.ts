import { element, by, ElementFinder } from 'protractor';

export class DevicesComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-devices div table .btn-danger'));
    title = element.all(by.css('jhi-devices div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class DevicesUpdatePage {
    pageTitle = element(by.id('jhi-devices-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    modelInput = element(by.id('field_model'));
    registeredInput = element(by.id('field_registered'));
    availabilityInput = element(by.id('field_availability'));
    typeInput = element(by.id('field_type'));
    departmentInput = element(by.id('field_department'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setModelInput(model) {
        await this.modelInput.sendKeys(model);
    }

    async getModelInput() {
        return this.modelInput.getAttribute('value');
    }

    async setRegisteredInput(registered) {
        await this.registeredInput.sendKeys(registered);
    }

    async getRegisteredInput() {
        return this.registeredInput.getAttribute('value');
    }

    async setAvailabilityInput(availability) {
        await this.availabilityInput.sendKeys(availability);
    }

    async getAvailabilityInput() {
        return this.availabilityInput.getAttribute('value');
    }

    async setTypeInput(type) {
        await this.typeInput.sendKeys(type);
    }

    async getTypeInput() {
        return this.typeInput.getAttribute('value');
    }

    async setDepartmentInput(department) {
        await this.departmentInput.sendKeys(department);
    }

    async getDepartmentInput() {
        return this.departmentInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class DevicesDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-devices-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-devices'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
