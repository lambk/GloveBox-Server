package Mappers;

import Model.Vehicle;
import Transfer.VehicleRegistrationDTO;

public class VehicleMapper implements IMapper<VehicleRegistrationDTO, Vehicle> {

    @Override
    public Vehicle map(VehicleRegistrationDTO source) {
        return new Vehicle(source.getPlate(), source.getMake(), source.getModel(), source.getYear(),
                source.getOdometer(), source.getWofExpiry(), source.getCountryRegistered());
    }
}
