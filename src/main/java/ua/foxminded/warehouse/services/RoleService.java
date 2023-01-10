package ua.foxminded.warehouse.services;
//package ua.foxminded.warehouse.servicies;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ua.foxminded.warehouse.models.Item;
//import ua.foxminded.warehouse.models.auth.Role;
//import ua.foxminded.warehouse.repositories.RoleRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional(readOnly = true)
//public class RoleService {
//    RoleRepository roleRepository;
//
//    @Autowired
//    public RoleService(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    public List<Role> findAll(){
//        return roleRepository.findAll();
//    }
//
//    public Role findById(int roleId){
//        Optional<Role> foundRole = roleRepository.findById(roleId);
//        return foundRole.orElse(null);
//    }
//
//    public Role findByName (String role) {
//        Optional<Role> foundRole = roleRepository.findByName(role);
//        return foundRole.orElse(null);
//    }
//
//    @Transactional
//    public void save (Role role){
//        roleRepository.save(role);
//    }
//
//    @Transactional
//    public void update (int roleId, Role updatedRole){
//        updatedRole.setId(roleId);
//        roleRepository.save(updatedRole);
//    }
//
//    @Transactional
//    public void delete (int roleId){
//        roleRepository.deleteById(roleId);
//    }
//}
