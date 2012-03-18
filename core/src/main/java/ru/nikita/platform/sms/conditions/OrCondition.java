package ru.nikita.platform.sms.conditions;

public class OrCondition<T> implements ICondition<T> {
    private ICondition<T> left;
    private ICondition<T> right;
    
    public OrCondition( ICondition<T> left, ICondition<T> right ) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isMatch(T object) {
        return ( this.left != null && this.left.isMatch(object) )
               || ( this.right != null && this.right.isMatch(object) );
    }
}