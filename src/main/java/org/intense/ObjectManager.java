package org.intense;

import org.intense.Types.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectManager {

    // Maps class/object ID -> list of member IDs
    private final HashMap<Integer, List<Integer>> Members = new HashMap<>();

    /**
     * Add a member to a class.
     * @param classId ID of the class/object
     * @param memberId ID of the member (variable, function, instance)
     */
    public void addMember(int classId, int memberId) {
        Members.computeIfAbsent(classId, k -> new ArrayList<>()).add(memberId);
    }

    /**
     * Get all members of a class.
     * @param classId class/object ID
     * @return List of member IDs (empty list if none)
     */
    public List<Integer> getMembers(int classId) {
        return Members.getOrDefault(classId, new ArrayList<>());
    }

    /**
     * Copy all members from a parent class to a child class.
     * Useful for inheritance.
     * @param parentId parent class ID
     * @param childId child class ID
     */
    public void copyMembers(int parentId, int childId) {
        List<Integer> parentList = Members.get(parentId);
        if (parentList != null) {
            Members.put(childId, new ArrayList<>(parentList));
        } else {
            Members.put(childId, new ArrayList<>());
        }
    }

    /**
     * Check if a class contains a specific member
     * @param classId class/object ID
     * @param memberId member ID
     * @return true if exists, false otherwise
     */
    public boolean hasMember(int classId, int memberId) {
        List<Integer> members = Members.get(classId);
        return members != null && members.contains(memberId);
    }

    /**
     * Remove a member from a class
     * @param classId class/object ID
     * @param memberId member ID
     */
    public void removeMember(int classId, int memberId) {
        List<Integer> members = Members.get(classId);
        if (members != null) {
            members.remove((Integer) memberId); // cast to Integer to remove by value
        }
    }

    public Value evalChain(String chain, Env env) {
        // Step 1: resolve root (e.g. "a.b.c")
        int rootId = env.getSymbolId(chain);
        Value obj = env.lookupById(rootId);
        if (obj == null) {
            throw new RuntimeException("Cannot access member of non-object: " + chain);
        }

        return obj;
    }

}

