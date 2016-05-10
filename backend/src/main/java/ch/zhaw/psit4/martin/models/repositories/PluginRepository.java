package ch.zhaw.psit4.martin.models.repositories;

import ch.zhaw.psit4.martin.models.Plugin;

public interface PluginRepository extends BaseRepository<Plugin, Integer>{
	// = @Query("SELECT Plugin p WHERE p.uuid = :uuid")
	Plugin findByUuid(String uuid);

}
