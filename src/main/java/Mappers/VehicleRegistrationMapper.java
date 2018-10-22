package Mappers;

import Model.Vehicle;

public class VehicleRegistrationMapper implements IMapper<Transfer.VehicleRegistrationDTO, Vehicle> {

    @Override
    public Vehicle map(Transfer.VehicleRegistrationDTO source) {
        return new Vehicle(source.getPlate(), source.getMake(), source.getModel(), source.getYear(),
                source.getOdometer(), source.getWofExpiry(), source.getCountryRegistered());
    }
}
