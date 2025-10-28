package storage;

import com.testproject.scoreboard.storage.InMemoryCollectionRepository;
import com.testproject.scoreboard.storage.Repository;
import com.testproject.scoreboard.storage.Storable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class InMemoryCollectionRepositoryTests {

    private Repository<FakeEntity, Integer> repository;

    @BeforeEach
    public void setup() {
        repository = new InMemoryCollectionRepository<>();
    }

    @Test
    public void save_simpleSave_shouldContainValue(){
        var entity = new FakeEntity(1, 5);

        repository.save(entity);

        var values = repository.getAll();
        assertThat(values).containsExactly(entity);
    }

    @Test
    public void save_multipleSave_shouldContainsAllValues(){
        var entity1 = new FakeEntity(1, 5);
        var entity2 = new FakeEntity(2, 5);

        repository.save(entity1);
        repository.save(entity2);

        var values = repository.getAll();
        assertThat(values).containsExactly(entity1, entity2);
    }

    @Test
    public void save_sameId_shouldReplace(){
        var entity1 = new FakeEntity(1, 5);
        var entity2 = new FakeEntity(1, 6);

        repository.save(entity1);
        repository.save(entity2);

        var values = repository.getAll();
        assertThat(values).containsExactly(entity2);
    }


    @Test
    public void removeById_removeOneOfMany_shouldRemoveCorrect(){
        var entity1 = new FakeEntity(1, 5);
        var entity2 = new FakeEntity(2, 6);
        var entity3 = new FakeEntity(3, 5);

        repository.save(entity1);
        repository.save(entity2);
        repository.save(entity3);
        repository.removeById(1);

        var values = repository.getAll();
        assertThat(values).containsExactly(entity2, entity3);
    }

    @Test
    public void findById_simpleCase_shouldReturnValue(){
        var entity1 = new FakeEntity(1, 5);
        var entity2 = new FakeEntity(2, 6);
        repository.save(entity1);
        repository.save(entity2);

        var result = repository.findById(2);

        assertThat(result).hasValue(entity2);
    }

    @Test
    public void findByPredicate_simpleCase_shouldReturnValue(){
        var entity1 = new FakeEntity(1, 5);
        var entity2 = new FakeEntity(2, 6);
        var entity3 = new FakeEntity(3, 10);
        repository.save(entity1);
        repository.save(entity2);
        repository.save(entity3);

        var result = repository.findByPredicate(e -> e.value >= 6);

        assertThat(result).containsExactly(entity2, entity3);
    }

    private record FakeEntity(Integer id, int value) implements Storable<Integer> {

        @Override
        public Integer getId() {
            return id();
        }
    }
}
