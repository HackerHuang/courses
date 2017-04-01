package list;

class LockDList extends DList {

    public void lockNode(DListNode node) {
        ((LockDListNode)node).isLocked = true;
    }

    @Override
    public void remove(DListNode node) {
        if(!((LockDListNode) node).isLocked) {
            super.remove(node);
        }
    }

    @Override
    protected DListNode newNode(Object item, DListNode prev, DListNode next) {
        return new LockDListNode(item, prev, next);
    }
}
