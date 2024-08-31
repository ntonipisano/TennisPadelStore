package com.pisano.tennispadelstore.model.dao;

import com.pisano.tennispadelstore.model.mo.Adminkey;
public interface AdminkeyDAO {
    public Adminkey create(
            String key);

    public void update (Adminkey adminkey);

    public void delete (Adminkey adminkey);
}
