/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DevicesComponentsPage, DevicesDeleteDialog, DevicesUpdatePage } from './devices.page-object';

const expect = chai.expect;

describe('Devices e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let devicesUpdatePage: DevicesUpdatePage;
    let devicesComponentsPage: DevicesComponentsPage;
    let devicesDeleteDialog: DevicesDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Devices', async () => {
        await navBarPage.goToEntity('devices');
        devicesComponentsPage = new DevicesComponentsPage();
        await browser.wait(ec.visibilityOf(devicesComponentsPage.title), 5000);
        expect(await devicesComponentsPage.getTitle()).to.eq('robcoApp.devices.home.title');
    });

    it('should load create Devices page', async () => {
        await devicesComponentsPage.clickOnCreateButton();
        devicesUpdatePage = new DevicesUpdatePage();
        expect(await devicesUpdatePage.getPageTitle()).to.eq('robcoApp.devices.home.createOrEditLabel');
        await devicesUpdatePage.cancel();
    });

    it('should create and save Devices', async () => {
        const nbButtonsBeforeCreate = await devicesComponentsPage.countDeleteButtons();

        await devicesComponentsPage.clickOnCreateButton();
        await promise.all([
            devicesUpdatePage.setNameInput('name'),
            devicesUpdatePage.setModelInput('5'),
            devicesUpdatePage.setRegisteredInput('registered'),
            devicesUpdatePage.setAvailabilityInput('availability'),
            devicesUpdatePage.setTypeInput('type'),
            devicesUpdatePage.setDepartmentInput('department')
        ]);
        expect(await devicesUpdatePage.getNameInput()).to.eq('name');
        expect(await devicesUpdatePage.getModelInput()).to.eq('5');
        expect(await devicesUpdatePage.getRegisteredInput()).to.eq('registered');
        expect(await devicesUpdatePage.getAvailabilityInput()).to.eq('availability');
        expect(await devicesUpdatePage.getTypeInput()).to.eq('type');
        expect(await devicesUpdatePage.getDepartmentInput()).to.eq('department');
        await devicesUpdatePage.save();
        expect(await devicesUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await devicesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Devices', async () => {
        const nbButtonsBeforeDelete = await devicesComponentsPage.countDeleteButtons();
        await devicesComponentsPage.clickOnLastDeleteButton();

        devicesDeleteDialog = new DevicesDeleteDialog();
        expect(await devicesDeleteDialog.getDialogTitle()).to.eq('robcoApp.devices.delete.question');
        await devicesDeleteDialog.clickOnConfirmButton();

        expect(await devicesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
