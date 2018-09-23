package Mappers;

public interface IMapper<S, D> {

    D map(S source);
}
