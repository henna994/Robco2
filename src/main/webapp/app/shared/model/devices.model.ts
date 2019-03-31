export interface IDevices {
    id?: number;
    name?: string;
    model?: number;
    registered?: string;
    availability?: string;
    type?: string;
}

export class Devices implements IDevices {
    constructor(
        public id?: number,
        public name?: string,
        public model?: number,
        public registered?: string,
        public availability?: string,
        public type?: string
    ) {}
}
