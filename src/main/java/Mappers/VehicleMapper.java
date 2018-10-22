package Mappers;

import Model.Vehicle;
import Transfer.VehicleDTO;

public class VehicleMapper implements IMapper<Vehicle, VehicleDTO> {
    @Override
    public VehicleDTO map(Vehicle source) {
        return new VehicleDTO(source.getId(), source.getPlate(), source.getMake(), source.getModel(), source.getYear(),
                source.getOdometer(), source.getWofExpiry(), source.getCountryRegistered());
    }
}
